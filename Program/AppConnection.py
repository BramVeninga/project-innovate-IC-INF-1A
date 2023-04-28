class AppConnection:
    def __init__(self):
        # connection is a boolean that can be used to determine if the connection to the app has been establist
        self.connection = False
        # data is a list that holds a the strings that should be sent to the app via bluetooth
        self.data = []
    
    # checks if a connection has been establist with the app
    def isConnected(self):
        # INSERT LOGIC HERE
        
        return self.connection
    
    # gathers all the data that has to be send
    def dataToSend(self):
        # INSERT LOGIC HERE
        
        return self.data
    
    # sends the data to the app
    def sendData(self):
        # INSERT LOGiC HERE
        
        return None