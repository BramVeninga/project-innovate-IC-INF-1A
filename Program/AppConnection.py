from machine import Pin, UART
from time import sleep

# This is a blueprint for an App Connection object.
# This object handles all the communication with the mobile application.
# It talks via bluetooth
class AppConnection:
    def __init__(self, uartPinTx, uartPinRx):
        # connection is a boolean that can be used to determine if the connection to the app has been establist
        # self.connection = False
        # data is a dictionary that holds the compartment data that will be sent to the mobile application
        self.data = {}
        self.uart = UART(1, baudrate=9600, bits=8, parity=None, stop=1, tx=Pin(uartPinTx), rx=Pin(uartPinRx))
        self.connectionPin = Pin(15, Pin.IN)
    
    # checks if a connection has been establist with the app
    def isConnected(self):
        # ask app for connected;
        self.uart.write('connection?;')
        sleep(1)
        
        bufferMessage = self.uart.read(0)
        while not self.uart.any() == 0:
            buffer = self.uart.read(1)
            bufferMessage += buffer
            if buffer == b';':
                tempMessage = bufferMessage
                bufferMessage = self.uart.read(0)
                self.uart.write('received;')
                if tempMessage == b'connected;':
                    return True
        return False
    
    # sends the data to the app
    def sendData(self):
        if self.listen() == b'send;':
            self.uart.write(str(self.data) + ";")
    
    # this function is used to gather information from the UART ports to determine if any and what data has been received from the bluetooth module.
    # on top of that, it will sent 'received' back to the device that send the data, if any was send.
    def listen(self):
        bufferMessage = self.uart.read(0)   
        while not self.uart.any() == 0:
            buffer = self.uart.read(1)
            bufferMessage += buffer
            if buffer == b';':
                tempMessage = bufferMessage
                bufferMessage = self.uart.read(0)
                self.uart.write('received;')
                print(tempMessage)
                return tempMessage
        return None

    # a function that can be used to send AT Commands to the bluetooth device.
    # the device should be rebooted in Command mode before this function is used.
    def atCommands(self, command):
        self.uart.deinit()
        self.uart.init(baudrate=38400, bits=8, parity=None, stop=1)
        self.uart.write(command)
        sleep(1)
        if not self.uart.any() == 0:
            buffer = self.uart.read()
            print(buffer)
        else:
            print('error')
            
        