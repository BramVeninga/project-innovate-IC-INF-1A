# This class serves as a blueprint for a sensor.
# This sensor is created with a pin number.
# The status of the sensor can also be checked, for if it's activated
class Sensor:
    #parameters:
    # - pinNumber, this is the number of the pin, of the raspberry pi pico, to which the sensor is connected
    def __init__(self, pinNumber):
        self.pinNumber = pinNumber
    
    # checks if the sensor is activated
    def isPressed(self):
        # INSERT LOGIC HERE
        
        return False