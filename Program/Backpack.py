from machine import Pin
from AppConnection import *

# This is a blueprint for a backpack.
# This backpack is the nexus of the program.
# It holds multiple compartments, a battery and an App Connection object.
class Backpack:
    # parameters:
    # - compartments, this is a tuple that holds al the Compartment objects that should be added to the bagpack
    def __init__(self, compartments):
        # bagContent is an tuple that holds the different Compartment objects
        self.bagContent = compartments
        # batteries is an list that holds the Battery objects
        self.batteries = []
        # appConneciton holds a single AppConneciton object
        self.appConnection = AppConnection()
        # led is an object that is used to talk to the onboard test led
        self.led = Pin('LED', Pin.OUT)
        self.led.off()
    
    # a function that is used to test inputs and outputs
    # NEEDS TO BE DELETED BEFORE DELIVERY
    def test(self):
        self.led.on()
        
        