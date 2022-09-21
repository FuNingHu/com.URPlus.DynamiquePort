#!/usr/bin/env python
# coding:utf-8

import sys
import time


from SimpleXMLRPCServer import SimpleXMLRPCServer
from SocketServer import ThreadingMixIn

myId = "9"
portID = 46666


def readPort():
	global portId
	f = open("/home/ur/ursim/ursim-5.11.0.108249/programs/port.dat","r")
	portId = f.readline()
	f.close()
	return int(portId)
	

sys.stdout.write("DynamiquePort daemon started")
sys.stderr.write("DynamiquePort daemon started")

class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
	pass

server = MultithreadedSimpleXMLRPCServer(("127.0.0.1", readPort()), allow_none=True)
server.RequestHandlerClass.protocol_version = "HTTP/1.1"

server.register_function(readPort, "readPort")
server.serve_forever()


