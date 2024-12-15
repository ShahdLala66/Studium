import time

import pyaudio
import numpy as np
import matplotlib.pyplot as plt

FORMAT = pyaudio.paInt16
SAMPLEFREQ = 44100
FRAMESIZE = 1024
NOFFRAMES = 220
p = pyaudio.PyAudio()
print('running')

stream = p.open(format=FORMAT, channels=1, rate=SAMPLEFREQ, input=True, frames_per_buffer=FRAMESIZE)
data = stream.read(NOFFRAMES * FRAMESIZE)
decoded = np.fromstring(data, 'int16');
#decoded = np.frombuffer(data, 'int16')

stream.stop_stream()
stream.close()
p.terminate()
print('done')

np.save("data/a1-a/zayne" + str(int(time.time())) + ".npy", decoded)

plt.figure(figsize=(10,6))
plt.plot(decoded)
plt.xlabel('Zeit')
plt.ylabel('Amplitude')
plt.savefig("data/a1-a/zayne" + str(int(time.time())) + ".png")
plt.grid(True)
plt.show()