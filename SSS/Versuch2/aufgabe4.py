import cv2
import numpy as np

path_dark = "./Bilder/Dunkelbilder/2_"
path_white = "./Bilder/Weissbilder/3_"

# Überprüfen auf Hot pixel
def get_hot_pixels(dark_img):
    hot_pixels = dark_img > 0
    return hot_pixels

# Überprüfen auf Stuck pixel
def get_stuck_pixels(dark_img, weiss_img):
    stuck_pixels = dark_img == weiss_img
    return stuck_pixels

# Überprüfen auf Dead pixel
def get_dead_pixels(weiss_img):
    dead_pixels = weiss_img == 0
    return dead_pixels

# Markieren der Pixel im Bild
def mark_pixels(image, pixels, color):
    if len(image.shape) == 2:  # Check if the image is grayscale
        marked_image = cv2.cvtColor(image.astype(np.uint8), cv2.COLOR_GRAY2BGR)
    else:
        marked_image = image
    marked_image[pixels] = color
    return marked_image

# Einlesen der Bilder
dark_image = cv2.imread("./Bilder/BilderNeu/dunkel_angepasst.png", cv2.IMREAD_GRAYSCALE)
white_image = cv2.imread("./Bilder/BilderNeu/weiss_angepasst.png", cv2.IMREAD_GRAYSCALE)

# Überprüfen und Markieren der Pixel
hot_pixels = get_hot_pixels(dark_image)
stuck_pixels = get_stuck_pixels(dark_image, white_image)
dead_pixels = get_dead_pixels(white_image)

marked_dark_image = mark_pixels(dark_image, hot_pixels, (0, 0, 255))  # Red for hot pixels
marked_dark_image = mark_pixels(marked_dark_image, stuck_pixels, (0, 255, 0))  # Green for stuck pixels
marked_white_image = mark_pixels(white_image, dead_pixels, (255, 0, 0))  # Blue for dead pixels

# Speichern der markierten Bilder
cv2.imwrite("./Bilder/BilderNeu/dark_image_marked.png", marked_dark_image)
cv2.imwrite("./Bilder/BilderNeu/white_image_marked.png", marked_white_image)