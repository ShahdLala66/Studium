import numpy as np
import matplotlib.pyplot as plt

# Frequenzen (in Hz)
frequencies = np.array([100, 200, 300, 400, 500, 700, 850, 1000, 1200, 1500, 1700, 3000, 4000, 5000, 6000, 10000])

# Amplituden und Phasenverschiebungen für Lautsprecher Groß
amplitude_large = np.array([48.30, 158.7, 87.11, 62.10, 60.38, 35.36, 34.5,
                            36.23, 33.64, 32.77, 34.5, 37.95, 50.89, 32.77, 25.01, 21.56])
phase_shift_large = np.array([5, 5.453, 3.664, 2.866, 2.336, 1.71, 1.408, 1.209,
                              1.018, 0.9376, 0.7383, 0.4438, 0.3757, 0.3416, 0.2772, 0.1291])

# Amplituden und Phasenverschiebungen für Lautsprecher Klein
amplitude_small = np.array([17.25, 26.74, 33.64, 50.89, 141.5, 82.8, 47.44,
                            45.71, 37.95, 40.54, 37.09, 35.36, 41.4, 22.43, 16.38, 25.87])
phase_shift_small = np.array([13.64, 8.109, 5.724, 4.34, 3.758, 1.429, 1.428, 1.233,
                              1.031, 0.8468, 0.7511, 0.4576, 0.3812, 0.3306, 0.2671, 0.1232])


# Plotten des Amplitudengangs
plt.figure(figsize=(10, 6))
plt.plot(frequencies, amplitude_large, label='Lautsprecher Groß', marker='o')
plt.plot(frequencies, amplitude_small, label='Lautsprecher Klein', marker='x')
plt.xlabel('Frequenz (Hz)')
plt.ylabel('Amplitude')
plt.title('Amplitudengang der Lautsprecher')
plt.legend()
plt.grid(True)

# Plotten des Phasengangs
plt.figure(figsize=(10, 6))
plt.plot(frequencies, phase_shift_large, label='Lautsprecher Groß', marker='o')
plt.plot(frequencies, phase_shift_small, label='Lautsprecher Klein', marker='x')
plt.xlabel('Frequenz (Hz)')
plt.ylabel('Phasenverschiebung (ms)')
plt.title('Phasengang der Lautsprecher')
plt.legend()
plt.grid(True)
plt.show()

# Berechnung der Amplitude in Dezibel
amplitude_large_db = 20 * np.log10(amplitude_large)
amplitude_small_db = 20 * np.log10(amplitude_small)

# Berechnung der Phasenwinkel
phase_angle_large = -phase_shift_large * frequencies * 360
phase_angle_small = -phase_shift_small * frequencies * 360

# Plotten des Bode-Diagramms
plt.figure(figsize=(12, 8))

# Amplitudengang
plt.subplot(2, 1, 1)
plt.semilogx(frequencies, amplitude_large_db, label='Lautsprecher Groß', marker='o')
plt.semilogx(frequencies, amplitude_small_db, label='Lautsprecher Klein', marker='x')
plt.xlabel('Frequenz (Hz)')
plt.ylabel('Amplitude (dB)')
plt.title('Bode-Diagramm - Amplitudengang')
plt.legend()
plt.grid(True, which='both', linestyle='--')

# Phasengang
plt.subplot(2, 1, 2)
plt.semilogx(frequencies, phase_angle_large, label='Lautsprecher Groß', marker='o')
plt.semilogx(frequencies,phase_angle_small, label='Lautsprecher Klein', marker='x')
plt.xlabel('Frequenz (Hz)')
plt.ylabel('Phasenverschiebung (ms)')
plt.title('Bode-Diagramm - Phasengang')
plt.legend()
plt.grid(True, which='both', linestyle='--')

plt.tight_layout()
plt.show()