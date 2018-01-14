from exponent_server_sdk import DeviceNotRegisteredError
from exponent_server_sdk import PushClient
from exponent_server_sdk import PushMessage
from exponent_server_sdk import PushResponseError
from exponent_server_sdk import PushServerError
from requests.exceptions import ConnectionError
from requests.exceptions import HTTPError

class PushServer:
    tokens = []
    def __init__(self):
        pass

    @staticmethod
    def add_new_token(token):
        for token2 in PushServer.tokens:
            if token == token2:
                return
        PushServer.tokens.append(token)

    @staticmethod
    def send_to_one(token, message, extra = None):
        try:
            response = PushClient().publish(
                PushMessage(to=token,
                            body=message,
                            data=extra))
        except PushServerError as exc:
            # Encountered some likely formatting/validation error.
            print(str(exc))
            raise
        except (ConnectionError, HTTPError) as exc:
            # Encountered some Connection or HTTP error - retry a few times in
            # case it is transient.
            raise exc

        try:
            # We got a response back, but we don't know whether it's an error yet.
            # This call raises errors so we can handle them with normal exception
            # flows.
            response.validate_response()
        except DeviceNotRegisteredError:
            # Mark the push token as inactive
            print(response)
            PushServer.tokens.remove(token)
        except PushResponseError as exc:
            # Encountered some other per-notification error.
            raise exc

    @staticmethod
    def send_to_all(message, extra=None):
        print("send_to_all, tokens = %s" % str(PushServer.tokens))
        for token in PushServer.tokens:
            PushServer.send_to_one(token, message, extra)

