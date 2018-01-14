from exponent_server_sdk import DeviceNotRegisteredError
from exponent_server_sdk import PushClient
from exponent_server_sdk import PushMessage
from exponent_server_sdk import PushResponseError
from exponent_server_sdk import PushServerError
from requests.exceptions import ConnectionError
from requests.exceptions import HTTPError

from urllib.request import urlopen, Request
import urllib
import json
import sys

class PushServer:
    tokens = []
    # am uitat sa salvez asta si fierbase-u nu isi lua token nou :(
    tokens_andro = ["fM-LNN1kqMg:APA91bEVLt5M5mzpnJyRtCbwblQXufqE34UDzXaCJqeIHk6SMOphD0KaznnFd16c7R2UxtOjOE9XnrWAMEzmIB4gnG_in-kjmJ9t22sAVlj9EVcSFq_wk7UIIX7tlHtqdsAzAS7tDljB"]
    def __init__(self):
        pass

    @staticmethod
    def add_new_token(token):
        for token2 in PushServer.tokens:
            if token == token2:
                return
        PushServer.tokens.append(token)

    @staticmethod
    def add_new_token_android(token):
        for token2 in PushServer.tokens_andro:
            if token == token2:
                return

        PushServer.tokens_andro.append(token)

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
    def send_to_one_andro(token, message, body = None):
        MY_API_KEY = "AIzaSyCdFV0bEUjOY3UzqAqKAbCtp37a7zTNkRQ"
        data = {"to": token,
               "notification": {"text": body, "title": message}}
        dataAsJSON = json.dumps(data)
        request = Request("https://fcm.googleapis.com/fcm/send", bytes(dataAsJSON, 'utf-8'),
                          {"Authorization": "key=" + MY_API_KEY, "Content-type": "application/json"})
        print(urlopen(request).read())


    @staticmethod
    def send_to_all(message, extra=None):
        print("send_to_all, tokens = %s" % str(PushServer.tokens))
        for token in PushServer.tokens:
            PushServer.send_to_one(token, message, extra)

        for token in PushServer.tokens_andro:
            PushServer.send_to_one_andro(token ,message, extra)

