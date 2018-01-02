from http.server import HTTPServer
from http_server.MobHttpServ import MobHttpServ
from update_server.UpdateServ import UpdateServ
import threading
import sys
import time

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

if __name__ == "__main__":
    t1 = threading.Thread(target=new_http_server)
    t2 = threading.Thread(target=new_upd_server)

    t1.start()
    t2.start()

    try:
        t1.join()
        t2.join()
    except KeyboardInterrupt:
        sys.exit(0)


