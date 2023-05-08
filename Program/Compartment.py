# This is a blueprint for a compartment of a backpack.
# This compartment has, often, multiple sensors and the compartment can checked if it's filled with something
class Compartment:
    # parameters:
    # - sensors, a tuple that holds all the sensors that are present in the compartment
    def __init__(self, sensors):
        self.sensors = sensors
    
    # checks if the compartment is filled with an physical object or not
    def isFilled(self):
        # INSERT LOGIC HERE
        
        return False