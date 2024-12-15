import numpy as np
import matplotlib.pyplot as plt

# Parameter: Beispieldaten
samplerate = 44100  # Samples pro Sekunde
duration = 1       # Dauer des Signals in Sekunden
threshold = 130   # Trigger-Schwelle (angepasst an Ihre Daten)

# 1. Laden der Daten
data = np.load("data/a2-b/a/tief/tief1733149940.npy")  # Ihre npy-Datei

# 2. Triggerzeitpunkt finden
trigger_index = np.argmax(np.abs(data) > threshold)  # Index, an dem das Signal den Schwellenwert überschreitet

# 3. Signal zuschneiden und auf 1s Länge bringen
start_index = trigger_index
end_index = start_index + samplerate  # 1 Sekunde Daten
signal_cropped = data[start_index:end_index]

# Falls das Signal kürzer ist, mit Nullen auffüllen
if len(signal_cropped) < samplerate:
    padding = samplerate - len(signal_cropped)
    signal_cropped = np.pad(signal_cropped, (0, padding), 'constant')

# 4. Darstellung des Signals zur Überprüfung
plt.figure(figsize=(10, 5))
plt.subplot(2, 1, 1)
plt.title("Originales Signal")
plt.plot(data)
plt.axvline(trigger_index, color='r', linestyle='--', label="Triggerzeitpunkt")
plt.legend()

plt.subplot(2, 1, 2)
plt.title("Signal nach Trigger und Zuschnitt (1s)")
plt.plot(signal_cropped)
plt.show()

# 5. Speichern des modifizierten Signals
np.save("d.npy", signal_cropped)
