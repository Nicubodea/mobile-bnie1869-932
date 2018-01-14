from http.server import BaseHTTPRequestHandler,HTTPServer
import cgi
import json
import jwt
from dbd.DAO import DAO
import hashlib

from http_server import PushServ


class MobHttpServ(BaseHTTPRequestHandler):

    def not_found(self):
        self.send_response(404)
        self.send_header("Content-type", "text/html")
        self.end_headers()
        self.wfile.write(b"404 - not found!")

    def get_response_message(self, status, key, key_text):
        message = {}
        message["status"] = status
        try:
            message[key] = str(key_text, 'utf-8')
        except:
            message[key] = key_text
        return message


    def send_error_with_reason(self, reason):
        self.send_response(400)
        self.send_header("Content-type", "application/json")
        self.end_headers()
        message = self.get_response_message("Error", "reason", reason)
        self.wfile.write(json.dumps(message).encode('utf-8'))
        return


    def send_success_with_key(self, key, key_text):
        self.send_response(200)
        self.send_header("Content-type", "application/json")
        self.end_headers()
        message = self.get_response_message("Success", key, key_text)
        self.wfile.write(json.dumps(message).encode('utf-8'))
        return


    ############################################# POSTS ############################################################
    def handle_post_register(self, postvars):
        try:
            if postvars[b'username'][0] is None or postvars[b'password'][0] is None:
                self.send_error_with_reason("Username or password not filled")
                return
        except:
            self.send_error_with_reason("Username or password not filled")
            return

        dao = DAO()
        passhash = hashlib.sha224(postvars[b'password'][0]).hexdigest()
        if not dao.add_user(postvars[b'username'][0].decode('utf-8'), passhash, 0):
            self.send_error_with_reason("Username with this name already exists")
            return

        self.send_success_with_key("reason", "ok")


    def handle_post_login(self, postvars):
        try:
            if postvars[b'username'][0] is None or postvars[b'password'][0] is None:
                self.send_error_with_reason("Username or password not filled")
                return
        except:
            self.send_error_with_reason("Username or password not filled")
            return

        dao = DAO()
        passhash = hashlib.sha224(postvars[b'password'][0]).hexdigest()
        user = dao.get_user_by_name(postvars[b'username'][0].decode('utf-8'))
        if user is None:
            self.send_error_with_reason("Username does not exist")
            return

        if passhash != user['passhash']:
            self.send_error_with_reason("Incorrect password")
            return

        self.send_success_with_key("token", jwt.encode(user, 'aceasta e cea mai buna parola', algorithm='HS256'))

    def _get_user_from_token(self, token):
        try:
            user = jwt.decode(token, 'aceasta e cea mai buna parola', algorithms=['HS256'])
            return user
        except:
            return None

    def handle_post_get_user(self, postvars):
        try:
            if postvars[b'token'][0] is None:
                self.send_error_with_reason("Please logout and login again")
                return
        except:
            self.send_error_with_reason("Please logout and login again")
            return

        user = self._get_user_from_token(postvars[b'token'][0].decode('utf-8'))
        if not user:
            self.send_error_with_reason("Please logout and login again")

        dao = DAO()

        usernew = dao.get_user_by_name(user["name"])

        self.send_success_with_key("user", json.dumps(usernew))


    def _transform_rentbike(self, x):
        dicti = {}
        dicti['street'] = x['street']
        try:
            dicti['active'] = 0 if x['active'] == 'Inactive' else 1
        except:
            dicti['active'] = 0
        try:
            dicti['total'] = x['numberOfBikes']
        except:
            dicti['total'] = x['total']
        try:
            dicti['available'] = x['numberOfAvailable']
        except:
            dicti['available'] = x['available']
        try:
            dicti['state'] = x['state']
        except:
            dicti['state'] = 'unchanged'
        return dicti

    def handle_post_create(self, postvars):
        token = postvars[b'token'][0].decode('utf-8')
        street = postvars[b'street'][0].decode('utf-8')
        total = postvars[b'total'][0].decode('utf-8')
        available = postvars[b'available'][0].decode('utf-8')
        active = postvars[b'active'][0].decode('utf-8')

        if active == "Active":
            active = 1
        else:
            active = 0

        try:
            user = jwt.decode(token, 'aceasta e cea mai buna parola', algorithms=['HS256'])
            if int(user['role']) != 1:
                raise Exception
        except:
            self.send_error_with_reason("Unauthorized!")
            return

        dao = DAO()

        if not dao.add_rent_bike(street, total, available, active):
            self.send_error_with_reason("Invalid data sent on create rentbike!")
        else:
            self.send_success_with_key("reason", "OK")


    def handle_post_merge(self, postvars):
        user_list = postvars[b'list'][0].decode('utf-8')
        user_list = json.loads(user_list)

        user_list = list(map(self._transform_rentbike, user_list))

        print("Received user list: %s" % str(user_list))

        dao = DAO()

        to_return = []

        for element in user_list:
            if element['state'] == 'created':
                dao.add_rent_bike(element['street'], element['total'], element['available'], element['active'])
            elif element['state'] == 'edited':
                dao.edit_rent_bike(0, element['street'], element['total'], element['available'], element['active'])
            elif element['state'] == 'deleted':
                dao.delete_rent_bike(element['street'])

        serv_list = dao.get_all_rent_bike()

        # find elements created and edited
        for element in serv_list:
            found = False
            for element_2 in user_list:
                if element['street'] == element_2['street']:
                    try:
                        if int(element['total']) != int(element_2['total']) or int(element['available']) != int(element_2['available']) or int(element['active']) != int(element_2['active']):
                            to_return.append(element)
                            to_return[len(to_return) - 1]['state'] = 'edited'
                    except:
                        if int(element['total']) != int(element_2['total']) or int(element['available']) != int(element_2['available']):
                            to_return.append(element)
                            to_return[len(to_return) - 1]['state'] = 'edited'
                    found = True

            if not found:
                to_return.append(element)
                to_return[len(to_return) - 1]['state'] = 'created'
        # now find deleted elements
        for element in user_list:
            found = False
            for element_2 in serv_list:
                if element['street'] == element_2['street']:
                    found = True
                    break
            if not found:
                to_return.append(element)
                to_return[len(to_return) - 1]['state'] = 'deleted'

        print(to_return)

        self.send_success_with_key("result", json.dumps(to_return))


    def handle_post_token(self, postvars):
        token = postvars[b'token'][0].decode('utf-8')
        PushServ.PushServer.add_new_token(token)
        self.send_success_with_key("reason", "OK")

    def handle_post_token_android(self, postvars):
        token = postvars[b'token'][0].decode('utf-8')
        PushServ.PushServer.add_new_token_android(token)
        self.send_success_with_key("reason", "OK")


    ############################################## GETS ##############################################################
    def handle_get_get_all(self):
        dao = DAO()

        r_list = dao.get_all_rent_bike()

        self.send_success_with_key("result", json.dumps(r_list))



    ############################################## DELETES ##########################################################
    def handle_delete_rbp(self, postvars):
        token = postvars[b'token'][0].decode('utf-8')
        street = postvars[b'street'][0].decode('utf-8')

        try:
            user = jwt.decode(token, 'aceasta e cea mai buna parola', algorithms=['HS256'])
            if int(user['role']) != 1:
                raise Exception
        except:
            self.send_error_with_reason("Unauthorized!")
            return

        dao = DAO()

        if not dao.delete_rent_bike(street):
            self.send_error_with_reason("Invalid data sent on delete rentbike!")
        else:
            self.send_success_with_key("reason", "OK")


    def handle_delete_rental(self, postvars):
        token = postvars[b'token'][0].decode('utf-8')
        street = postvars[b'street'][0].decode('utf-8')

        try:
            user = jwt.decode(token, 'aceasta e cea mai buna parola', algorithms=['HS256'])
            if int(user['role']) != 0:
                raise Exception
        except:
            self.send_error_with_reason("Unauthorized!")
            return

        dao = DAO()
        if not dao.delete_rental(user['id'], street):
            self.send_error_with_reason("Invalid data sent on delete rental!")
        else:
            self.send_success_with_key("reason", "OK")

    ################################################### PUTS #######################################################
    def handle_put_edit_rbp(self, postvars):
        token = postvars[b'token'][0].decode('utf-8')
        street = postvars[b'street'][0].decode('utf-8')
        total = postvars[b'total'][0].decode('utf-8')
        available = postvars[b'available'][0].decode('utf-8')
        active = bool(postvars[b'active'][0].decode('utf-8'))

        try:
            user = jwt.decode(token, 'aceasta e cea mai buna parola', algorithms=['HS256'])
            if int(user['role']) != 1:
                raise Exception
        except:
            self.send_error_with_reason("Unauthorized!")
            return

        dao = DAO()

        if not dao.edit_rent_bike(0, street, total, available, active):
            self.send_error_with_reason("Invalid data sent on create rentbike!")
        else:
            self.send_success_with_key("reason", "OK")


    def handle_put_add_rental(self, postvars):
        token = postvars[b'token'][0].decode('utf-8')
        street = postvars[b'street'][0].decode('utf-8')

        try:
            user = jwt.decode(token, 'aceasta e cea mai buna parola', algorithms=['HS256'])
            if int(user['role']) != 0:
                raise Exception
        except:
            self.send_error_with_reason("Unauthorized!")
            return

        dao = DAO()
        if not dao.add_rental(user['id'], street):
            self.send_error_with_reason("Invalid data sent on delete rental!")
        else:
            self.send_success_with_key("reason", "OK")

    ######################################## HANDLE METHODS #########################################################

    def do_GET(self):
        if self.path == "/get_all":
            self.handle_get_get_all()
        else:
            self.send_response(404)
            self.send_header("Content-type", "text/html")
            self.end_headers()
            self.wfile.write(b"404 - not found!")

    def do_POST(self):
        ctype, pdict = cgi.parse_header(self.headers['Content-type'])
        if ctype == 'multipart/form-data':
            postvars = cgi.parse_multipart(self.rfile, pdict)
        elif ctype == 'application/x-www-form-urlencoded':
            length = int(self.headers['Content-length'])
            postvars = cgi.parse_qs(self.rfile.read(length), keep_blank_values=1)
        else:
            postvars = {}

        if self.path == "/register":
            self.handle_post_register(postvars)

        elif self.path == "/login":
            self.handle_post_login(postvars)

        elif self.path == "/get_my_user":
            self.handle_post_get_user(postvars)

        elif self.path == "/add_new_rbp":
            self.handle_post_create(postvars)

        elif self.path == "/merge":
            self.handle_post_merge(postvars)

        elif self.path == "/token":
            self.handle_post_token(postvars)

        elif self.path == "/token_android":
            self.handle_post_android_token(postvars)

        elif self.path == "/delete_rbp":
            self.handle_delete_rbp(postvars)
        else:
            self.not_found()

        return

    def do_DELETE(self):
        ctype, pdict = cgi.parse_header(self.headers['Content-type'])
        if ctype == 'multipart/form-data':
            postvars = cgi.parse_multipart(self.rfile, pdict)
        elif ctype == 'application/x-www-form-urlencoded':
            length = int(self.headers['Content-length'])
            postvars = cgi.parse_qs(self.rfile.read(length), keep_blank_values=1)
        else:
            postvars = {}

        if self.path == "/delete_rbp":
            self.handle_delete_rbp(postvars)
        elif self.path == "/delete_rental":
            self.handle_delete_rental(postvars)
        else:
            self.not_found()


    def do_PUT(self):
        ctype, pdict = cgi.parse_header(self.headers['Content-type'])
        if ctype == 'multipart/form-data':
            postvars = cgi.parse_multipart(self.rfile, pdict)
        elif ctype == 'application/x-www-form-urlencoded':
            length = int(self.headers['Content-length'])
            postvars = cgi.parse_qs(self.rfile.read(length), keep_blank_values=1)
        else:
            postvars = {}
        if self.path == "/edit_rbp":
            self.handle_put_edit_rbp(postvars)
        elif self.path == "/create_rental":
            self.handle_put_add_rental(postvars)
        else:
            self.not_found()


