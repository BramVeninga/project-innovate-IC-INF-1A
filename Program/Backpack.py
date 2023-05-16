from Compartment import *
from machine import Pin
from AppConnection import *

# This is a blueprint for a backpack.
# This backpack is the nexus of the program.
# It holds multiple compartments, a battery and an App Connection object.
class Backpack:
    # parameters:
    # - compartments, this is a tuple that holds al the Compartment objects that should be added to the bagpack
    def __init__(self):
        # bagContent is an tuple that holds the different Compartment objects
        self.bagContent = []
        # batteries is an list that holds the Battery objects
        self.batteries = []
        # appConneciton holds a single AppConneciton object
        self.appConnection = AppConnection(8, 9)
        # led is an object that is used to talk to the onboard test led
        self.led = Pin('LED', Pin.OUT)
        self.led.off()
    
    def addCompartment(self, sensors, compartmentCount):
        compartment = Compartment(compartmentCount)
        self.bagContent.append(compartment)
        for sensor in sensors:
            self.bagContent[compartmentCount].sensors.append(sensor)
    
    def addMultipleCompartments(self, sensors):
        count = 0
        for tempSensors in sensors:
            self.addCompartment(tempSensors, count)
            count += 1
    
    # a function that is used to test inputs and outputs
    # NEEDS TO BE DELETED BEFORE DELIVERY
    def test(self):
        self.checkContents()
        print(self.appConnection.data)
        self.appConnection.sendData()
#         print(self.appConnection.isConnected())
#         if self.appConnection.isConnected():
#             self.led.on()
    
    def checkContents(self):
        self.appConnection.data.clear()
        for compartment in tuple(self.bagContent):
            compartment.isFilled()
            data = {}
            data[str(compartment.id)] = compartment.filled
            self.appConnection.data.update(**data)
