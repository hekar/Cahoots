#!/usr/bin/python
#
# Requires:
#   https://github.com/liris/websocket-client - install from source released version is buggy
#   http://docs.python-requests.org/en/latest/
#

import websocket
import requests
import thread
import time
import sys
import os
import json
from time import gmtime, strftime

def on_message(ws, message):
    print 'message ('+ strftime("%Y-%m-%d %H:%M:%S", gmtime()) +'):'
    print json.dumps(json.loads(message), sort_keys=True, indent=4)

def on_error(ws, error):
    print 'error:'
    print error

def on_close(ws):
    print "### closed ###"

def on_open(ws):
    print '### opened ###'

if __name__ == "__main__":
    authToken = requests.post(
        "http://localhost:9000/app/login", data={
            'username': str(os.getpid()),
            'password': str(os.getpid())
        }).text
    print "Logged in as {0}:{1}".format(os.getpid(), authToken)
    websocket.enableTrace(False)
    ws = websocket.WebSocketApp('ws://localhost:9000/app/message?auth_token=' + authToken,
            on_message = on_message,
            on_error = on_error,
            on_close = on_close)
    ws.on_open = on_open
    ws.run_forever()