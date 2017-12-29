import socket
from dbd.DAO import DAO

class UpdateServ:
    def __init__(self):
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

