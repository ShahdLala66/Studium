import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.ticker import MaxNLocator, FormatStrFormatter


df = pd.read_csv('Data/Harmonika.csv', delimiter=';')

# Replace comma with dot in the 'Zeit' column
df['Zeit'] = df['Zeit'].str.replace(',', '.')
df['Kanal A'] = df['Kanal A'].str.replace(',', '.')

# Convert the 'Zeit' column to numeric
time_ms = pd.to_numeric(df['Zeit'].values[2:])
signal_mv = df['Kanal A'].values[2:]

print(time_ms)
print(signal_mv)

# Convert time to seconds
time_s = time_ms / 1000

plt.plot(time_s, signal_mv)
plt.xlabel('Zeit (s)')
plt.ylabel('Amplitude (mV)')
plt.title('Harmonika Signal')
plt.grid(True)
plt.gca().yaxis.set_major_locator(MaxNLocator(nbins=10))

plt.show()

# Calculate the signal duration
signal_duration = time_s[-1] - time_s[0]
print(f'Signaldauer: {signal_duration:.4f}s')

# Calculate the sampling frequency
sampling_frequency = len(time_s) / signal_duration
print(f'Abtastfrequenz: {sampling_frequency:.4f}Hz')

# Calculate the signal length in number of samples
signal_length = len(time_s)
print(f'Signallänge M: {signal_length}')

# Calculate the sampling interval
sampling_interval = signal_duration / signal_length
print(f'Abtastintervall ∆t: {sampling_interval:.8f}s')

# Perform FFT on the signal
signal_fft = np.fft.fft(signal_mv)

# Calculate the frequency axis
frequencies = np.fft.fftfreq(signal_length, sampling_interval)

# Calculate the amplitude spectrum
amplitudes = np.abs(signal_fft)

# Plot only the positive frequencies
plt.plot(frequencies[:signal_length // 2], amplitudes[:signal_length // 2])
plt.xlabel('Frequenz (Hz)')
plt.ylabel('Amplitude')
plt.title('Fourier Transformation')
plt.grid(True)
plt.show()