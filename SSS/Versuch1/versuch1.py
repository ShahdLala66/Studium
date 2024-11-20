import os
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import math


def calibration():
    result = {}
    folder_path = r"C:\Users\coreu\Documents\#Studium\Sensoren\Messungen"

    all_files = os.listdir(folder_path)
    i = 10
    for file in all_files:
        file_path = os.path.join(folder_path, file)
        df = pd.read_csv(file_path, delimiter=";", decimal=",", skiprows=1000, nrows=1000, usecols=[1])
        data = df.iloc[:, 0].to_numpy()
        mean = np.mean(data)
        std = np.std(data) / math.sqrt(len(data))
        print(f"{i}cm: \t mean = {mean:.4f} \t std = {std:.4f}")
        result[i] = mean
        i += 3

    return result


def plotcalibration(calibration):
    y = list(calibration.keys())
    x = list(calibration.values())
    plt.figure()
    plt.plot(x, y, 'ro', label='Gemessene Daten')
    plt.xlabel('Entfernung (in cm)')
    plt.ylabel('Mittelwert (in V)')
    plt.title('Mittelwerte der Messungen')
    plt.legend()
    plt.grid(True)


def lineareRegressionPlot(calibration):
    y = list(calibration.keys())
    x = list(calibration.values())
    y_log, x_log = [], []

    for i in range(20):
        lny = math.log(y[i])
        lnx = math.log(x[i])
        y_log.append(lny)
        x_log.append(lnx)

    plt.figure()
    plt.plot(x_log, y_log, 'ro', label='Logarithmierte Daten')
    plt.xlabel('x_log')
    plt.ylabel('y_log')

    nx = np.array(x_log)
    ny = np.array(y_log)
    ny_mean = np.mean(ny)
    nx_mean = np.mean(nx)
    a = np.sum((nx - nx_mean) * (ny - ny_mean)) / np.sum((nx - nx_mean) ** 2)
    b = ny_mean - a * nx_mean

    x_reg = np.linspace(min(nx), max(nx), 100)
    y_reg = a * x_reg + b

    plt.plot(x_reg, y_reg, 'b-', label='Regressionsgerade')
    plt.legend()
    plt.title('Ausgleichsgerade')
    plt.grid(True)
    plt.show()

    x_reg = np.linspace(min(x), max(x), 100)
    y_reg = np.exp(a * np.log(x_reg) + b)

    plt.figure()
    plt.plot(x, y, 'ro', label='Gemessene Daten')
    plt.xlabel('Entfernung (in cm)')
    plt.ylabel('Mittelwert (in V)')
    plt.plot(x_reg, y_reg, 'b-', label='Regression')
    plt.legend()
    plt.title('Mittelwerte der Messungen')
    plt.grid(True)
    plt.show()


def lineareRegression(calibration, z):
    y = list(calibration.keys())
    x = list(calibration.values())
    y_log, x_log = [], []

    for i in range(20):
        lny = math.log(y[i])
        lnx = math.log(x[i])
        y_log.append(lny)
        x_log.append(lnx)

    nx = np.array(x_log)
    ny = np.array(y_log)
    ny_mean = np.mean(ny)
    nx_mean = np.mean(nx)
    a = np.sum((nx - nx_mean) * (ny - ny_mean)) / np.sum((nx - nx_mean) ** 2)
    b = ny_mean - a * nx_mean

    messfehlercm68 = z[0] + 1 * z[1]
    messfehlercm95 = z[0] + 1.96 * z[1]
    relk = messfehlercm95 / z[0]

    abstand = math.exp(a * math.log(z[0]) + b)
    deltaAbstand = math.exp(b) * a * math.pow(z[0], a - 1) * z[1]

    result = [abstand, deltaAbstand, messfehlercm68, messfehlercm95, relk]
    return result


def abstandDaten(folder_path):
    result = {}
    all_files = os.listdir(folder_path)
    for file in all_files:
        file_path = os.path.join(folder_path, file)
        df = pd.read_csv(file_path, delimiter=";", decimal=",", skiprows=1000, nrows=1000, usecols=[1])
        data = df.iloc[:, 0].to_numpy()
        mean = np.mean(data)
        std = np.std(data) / math.sqrt(len(data))
        result[file] = [mean, std]
    return result


def flaechenberechnung(a, b):
    F = math.sqrt((b[0] * a[1]) ** 2 + (a[0] * b[1]) ** 2)
    A = a[0] * b[0]
    return [A, F]


print("Versuch 1:\n")
calibration = calibration()
plotcalibration(calibration)
lineareRegressionPlot(calibration)
path = r"C:\Users\coreu\Documents\#Studium\Sensoren\MessungenBlatt"
papier = abstandDaten(path)
a = lineareRegression(calibration, papier["breite.csv"])
b = lineareRegression(calibration, papier["länge.csv"])
A = flaechenberechnung(a, b)

print(f"\nLange Seite 68%: {a[0]:.4f} cm +- 1 * {a[2]:.4f}")
print(f"Lange Seite 95%: {a[0]:.4f} cm +- 1.96 * {a[3]:.4f}")
print(f"relativer Fehler: {a[4]:.4f}")
print(f"\nKurze Seite 68%: {b[0]:.4f} cm +- 1 * {b[2]:.4f}")
print(f"Kurze Seite 95%: {b[0]:.4f} cm +- 1.96 * {b[3]:.4f}")
print(f"relativer Fehler: {b[4]:.4f}")
print(f"\nFläche 68%: {A[0]:.4f} cm^2 +- 1 * {A[1]:.4f}")
print(f"Fläche 95%: {A[0]:.4f} cm^2 +- 1.96 * {A[1]:.4f}")
print(f"Δy kurze Seite: {b[1]:.4f}")
print(f"Δy lange Seite: {a[1]:.4f}")
print(f"Fehlerfortpflanzung Fläche: {A[1]:.4f}")
