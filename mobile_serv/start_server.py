from http.server import HTTPServer
from http_server.MobHttpServ import MobHttpServ

if __name__ == "__main__":
    server_class = HTTPServer
    httpd = server_class(('', 80), MobHttpServ)
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    httpd.server_close()