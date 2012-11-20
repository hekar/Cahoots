#!/usr/bin/python

import requests
import os
import time

if __name__ == "__main__":
    while True:
        authToken = requests.post(
            "http://localhost:9000/app/login", data={
                'username': str(os.getpid()),
                'password': str(os.getpid())
            }).text
        print "Logged in as {0}:{1}".format(os.getpid(), authToken)
        time.sleep(5)
        requests.post(
        "http://localhost:9000/app/logout", data={
            'auth_token': authToken
        }).text
        print "Logged out"
        time.sleep(5)

