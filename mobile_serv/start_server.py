from http.server import HTTPServer
from http_server.MobHttpServ import MobHttpServ
from update_server.UpdateServ import UpdateServ
import threading
import sys
import time
from dbd.DAO import DAO

def new_http_server():
    server_class = HTTPServer
    httpd = server_class(('', 80), MobHttpServ)
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    httpd.server_close()

def new_upd_server():
    upd_serv = UpdateServ()


def client_sim():
    dao = DAO()
    last = ""
    while True:
        try:

            command = int(input())
            if command == 1:
                last = "scramble"+str(time.time())
                dao.add_rent_bike(last, 2,1,1)
            elif command == 2:
                dao.edit_rent_bike(0, last, 3,2,1)
                dao.edit_rent_bike(0, last,2,1,1)
            elif command == 3:
                dao.delete_rent_bike(last)
        except:
            pass


if __name__ == "__main__":
    t1 = threading.Thread(target=new_http_server)
    t2 = threading.Thread(target=new_upd_server)
    t3 = threading.Thread(target=client_sim)

    t1.start()
    t2.start()
    t3.start()

    try:
        t1.join()
        t2.join()
        t3.join()
    except KeyboardInterrupt:
        sys.exit(0)




