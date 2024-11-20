import cv2
import numpy as np

path_dark = "./Bilder/Dunkelbilder/2_"
path_white = "./Bilder/Weissbilder/3_"
path_gwk = "./Bilder/Grauwertkeil/1_"

# Routine zum Einlesen der Bilder als Grauwertbilder
def get_image(image_path, number_images):
    # Array für die Bilder
    img_arr = []

    for i in range(number_images):
        img_read = cv2.imread(image_path + str(i) + ".png", cv2.IMREAD_GRAYSCALE)
        img_arr.append(img_read)

    # pixelweisen Mittelwert berechnen (double)
    return np.mean(img_arr, axis=0).astype(np.double)


# Funktion zum Entfernen von Rauschen
def fix_noise(dark_img, white_img, input_img):
    # Dunkelbild von Eingabebild abziehen
    input_img = input_img - dark_img
    # Weißbild normieren, sodass sein Mittelwert 1 ist
    white_img = (white_img - dark_img) / np.mean(white_img)
    # Eingabebild durch normiertes Weißbild teilen
    input_img = input_img / white_img
    # korrigiertes Bild speichern
    cv2.imwrite("./Bilder/Grauwertkeil/Gwk_korrigiert.png", input_img)
    return input_img

# Einlesen der Bilder
dark_image = get_image(path_dark, 10)
white_image = get_image(path_white, 10)
input_image = get_image(path_gwk, 1)

# Rauschunterdrückung
res = fix_noise(dark_image, white_image, input_image)
cv2.imshow("Ergebnis", res.astype(np.uint8))


dunkel_angepasst = dark_image * 50
weiss_angepasst = white_image * 50

# Bilder anzeigen
cv2.imshow('original_dunkel', dark_image)
cv2.imshow('dunkel_angepasst', dunkel_angepasst)
cv2.imshow('original_weiss', white_image)
cv2.imshow('weiss_angepasst', weiss_angepasst)

cv2.imwrite("./Bilder/BilderNeu/dunkel_angepasst.png", dunkel_angepasst)
cv2.imwrite("./Bilder/BilderNeu/weiss_angepasst.png", weiss_angepasst)
cv2.imwrite("./Bilder/BilderNeu/dunkel_original.png", dark_image)
cv2.imwrite("./Bilder/BilderNeu/weiss_original.png", white_image)

cv2.waitKey(0)
cv2.destroyAllWindows()