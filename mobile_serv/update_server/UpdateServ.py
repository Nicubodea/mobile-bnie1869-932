from websocket_server import WebsocketServer

class UpdateServ:
    def new_client(self, client, server):
        print("New client connected " + str(client))

    server = None
    def __init__(self):
        UpdateServ.server = WebsocketServer(7474, host='0.0.0.0')
        UpdateServ.server.set_fn_new_client(self.new_client)
        UpdateServ.server.run_forever()

    @staticmethod
    def notify_observers(operation):
        print("Sending message %s" % operation)
        UpdateServ.server.send_message_to_all(operation)

