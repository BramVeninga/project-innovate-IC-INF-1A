from Backpack import *
from Sensor import *
from Compartment import *
from time import sleep
from machine import Pin

# sensorsComp0 = (Sensor(0), Sensor(1))
# sensorsComp1 = (Sensor(2))
compartment0 = Compartment(0)
compartment0.sensors.append(Sensor(0))
compartment0.sensors.append(Sensor(1))

compartment1 = Compartment(1)
compartment1.sensors.append(Sensor(2))

backpack = Backpack()
backpack.bagContent.append(compartment0)
backpack.bagContent.append(compartment1)

while 1:  
    backpack.test()
    sleep(1)

# led = Pin('LED', Pin.OUT)
# led.off()
# 
# input = Pin(2, Pin.IN, Pin.PULL_UP)
# while 1:
#     print(input.value())
#     sleep(1)