import cv2
import numpy as np
from tabulate import tabulate

def slice_image(image_path, step=20, threshold=10):
    image_array = []

    img_read = cv2.imread(image_path, cv2.IMREAD_GRAYSCALE)
    if img_read is None:
        return []

    image_array.append(img_read)
    img = np.mean(image_array, axis=0, dtype=float)

    subarrays = []

    for n in range(0, len(img[0]) - step, step):
        if abs(int(img[0, n + step]) - int(img[0, n])) > threshold:
            start_index = max(n - 99, 0)
            subarrays.append(img[:, start_index:n])

    subarrays.append(img[:, len(img[0]) - 99:len(img[0]) - 1])

    return subarrays

def fix_noise(dark_img, white_img, input_img):
    input_img = input_img - dark_img
    white_img = (white_img - dark_img) / np.mean(white_img)
    input_img = input_img / white_img
    return input_img

def get_image(image_path):
    image_arr = []

    for j in range(10):
        img_read = cv2.imread(image_path + str(j) + ".png", cv2.IMREAD_GRAYSCALE)
        if img_read is not None:
            image_arr.append(img_read)

    if not image_arr:
        return np.array([])

    return np.mean(image_arr, axis=0).astype(np.double)

sub_arrays = slice_image("./Bilder/Grauwertkeil/1_0.png")


res_mean = []
res_std = []
i = 0

table = []
for i, x in enumerate(sub_arrays):
    if x.size > 0:
        mean = np.round(np.mean(x), 2)
        std = np.round(np.std(x), 2)
    else:
        mean = np.nan
        std = np.nan

    res_mean.append(mean)
    res_std.append(std)

    table.append([i, mean, std])

print(tabulate(table, headers=["Index", "Mean", "Std"],
               tablefmt="simple", colalign=("left", "left", "left")))


sub_arrays_fixed = slice_image("./Bilder/Grauwertkeil/Gwk_korrigiert.png", 20, 35)

res_mean_fixed = []
res_std_fixed = []
j = 0

table_fixed = []
for j, x in enumerate(sub_arrays_fixed):
    if x.size > 0:
        mean = np.round(np.mean(x), 2)
        std = np.round(np.std(x), 2)
    else:
        mean = np.nan
        std = np.nan

    res_mean_fixed.append(mean)
    res_std_fixed.append(std)

    table_fixed.append([j, mean, std])

print(tabulate(table_fixed, headers=["Index", "Mean", "Std"],
               tablefmt="simple", colalign=("left", "left", "left")))

for i in range(len(sub_arrays)):
    if sub_arrays[i].size > 0:
        cv2.imshow("Grauwert " + str(i), sub_arrays[i].astype(np.uint8))

for i in range(len(sub_arrays_fixed)):
    if sub_arrays_fixed[i].size > 0:
        cv2.imshow("Grauwert Fixed " + str(i), sub_arrays_fixed[i].astype(np.uint8))

cv2.waitKey(0)
cv2.destroyAllWindows()