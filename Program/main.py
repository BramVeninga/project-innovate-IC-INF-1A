from Backpack import *
from Sensor import *
from Compartment import *
from time import sleep
from machine import Pin, UART

# lists that hold all the sensors that are in use
sensorsComp0 = (Sensor(0), )
sensorsComp1 = (Sensor(1), Sensor(2))
sensorsComp2 = (Sensor(3), Sensor(4))
sensorsComp3 = (Sensor(5), )

# the matrix that holds all the lists of sensors.
# this matrix is used in the Backpack.addMultipleCompartments function
sensors = (sensorsComp0, sensorsComp1, sensorsComp2, sensorsComp3)

backpack = Backpack()
backpack.addMultipleCompartments(sensors)

while 1:
    backpack.checkContents()
    backpack.appConnection.sendData()
    sleep(1)