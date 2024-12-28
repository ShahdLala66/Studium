# This is a sample Python script.

import redlab as rl

print("------- einzelne Werte -------------------------")
print("16 Bit Value: " + str(rl.cbAIn(0, 0, 1)))
print("Voltage Value: " + str(rl.cbVIn(0, 0, 1)))
print("------- Messreihe -------------------------")
print("Messreihe: " + str(rl.cbAInScan(0, 0, 0, 300, 8000, 1)))
print("Messreihe: " + str(rl.cbVInScan(0, 0, 0, 1000, 8000, 1)))
print("------- Ausgabe -------------------------")
print("Voltage Value: " + str(rl.cbVOut(0, 0, 101, 5.0815)))

result = rl.cbVInScan(0, 0, 0, 1000, 8000, 1)
with open("8000.txt", 'w') as f:
    # Abstastsfrequenz 2000, Sylus = 571, 2000-8000. schritt 857
    for d in result:
        f.write(str(d) + ",")
    f.flush()
