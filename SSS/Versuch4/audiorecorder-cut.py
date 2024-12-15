import time
import pyaudio
import numpy as np
import matplotlib.pyplot as plt

FORMAT = pyaudio.paInt16
SAMPLEFREQ = 44100  # Sampling frequency
FRAMESIZE = 1024  # Number of frames per buffer
NOFFRAMES = 220  # Number of frames to record (approx 5 seconds)

# Initialize PyAudio
p = pyaudio.PyAudio()
print('running')

stream = p.open(format=FORMAT, channels=1, rate=SAMPLEFREQ, input=True, frames_per_buffer=FRAMESIZE)

# Record data
data = stream.read(NOFFRAMES * FRAMESIZE)
decoded = np.frombuffer(data, 'int16')  # Decode byte data to numpy array

stream.stop_stream()
stream.close()
p.terminate()
print('done')

# Save raw data
timestamp = int(time.time())
np.save(f"data/a2-a/bonus/bonus{timestamp}.npy", decoded)

# Trigger function
threshold = 0.1 * np.max(np.abs(decoded))  # Set threshold relative to max amplitude
start = np.argmax(np.abs(decoded) > threshold) - FRAMESIZE  # Trigger point (adjust by frame size)
start = max(start, 0)  # Ensure non-negative start index

# Define 1-second segment

end = start + SAMPLEFREQ
triggered = decoded[start:end]

# Fill with zeros if length is less than 1 second
if len(triggered) < SAMPLEFREQ:
    triggered = np.concatenate((triggered, np.zeros(SAMPLEFREQ - len(triggered), dtype='int16')))

# Save triggered segment visualization
plt.figure(figsize=(10, 6))
plt.plot(triggered)
plt.title('Triggered Audio Signal')
plt.xlabel('Time (samples)')
plt.ylabel('Amplitude')
plt.grid(True)
plt.savefig(f"data/a2-a/bonus/bonus{timestamp}.png")
plt.show()
