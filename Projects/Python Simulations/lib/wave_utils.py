import numpy as np
from scipy.signal import find_peaks
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from IPython.display import HTML
import sounddevice as sd

# x_values - 1d array with x values
# y_values - 2d array [time_index, x_index]
def showAnim(title, x_values, y_values, x_label = "x", y_label = "", max_frames_to_render = 500):
    # figure out range of x and y
    x_range = (float(x_values.min()), float(x_values.max()))
    y_range = (float(y_values.min()), float(y_values.max()))
    
    # create the figure and axis
    fig, ax = plt.subplots()
    line, = ax.plot(x_values, y_values[0, :], lw=2)
    ax.set_xlim(x_range[0], x_range[1])
    ax.set_ylim(1.2 * y_range[0], 1.2 * y_range[1])
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    ax.set_title(title)
    
    # deal with max_frames_to_render
    frame_inc = 1
    frameCount = y_values.shape[0]
    while frameCount / frame_inc > max_frames_to_render:
        frame_inc += 1
    frameCount = int(frameCount / frame_inc)

    # update function for animation
    def updateGraph(frame):
        line.set_ydata(y_values[frame * frame_inc, :])
        return line,

    # create the animation
    anim = FuncAnimation(fig, updateGraph, frames=frameCount, interval=15, blit=True)
    plt.close()
    return anim

# x_values - 1d array of x values
# y_values - 1d array of y values
def showGraph(title, x_values, y_values, x_label = "x", y_label = ""):
    # figure out range of x and y
    x_range = (float(x_values.min()), float(x_values.max()))
    y_range = (float(y_values.min()), float(y_values.max()))

    # create the figure and axis
    fig, ax = plt.subplots()
    ax.plot(x_values, y_values, lw=2)
    ax.set_xlim(x_range[0], x_range[1])
    ax.set_ylim(1.2 * y_range[0], 1.2 * y_range[1])
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)
    ax.set_title(title)
    plt.show()

# estimates frequency by finding the peaks and averaging the difference between
# values - 1d array of values
def calcSingalFrequency(values, dt):
    peaks, _ = find_peaks(values)
    if len(peaks) > 1:
        peak_times = peaks * dt
        periods = np.diff(peak_times)
        avg_period = np.mean(periods)
        frequency = 1 / avg_period
        return frequency
    # not enough peaks to estimate frequency
    return -1.0

# play a note/sound at a specific frequency (sine wave signal)
def playNote(frequency, duration=1.5, amplitude=0.5, sample_rate=44100, fade_percentage=0.2):
    t = np.linspace(0, duration, int(sample_rate * duration), False)
    note = amplitude * np.sin(2 * np.pi * frequency * t)

    # calculate fade samples & create envelope
    fade_samples = int(sample_rate * duration * fade_percentage)
    fade_in = np.linspace(0, 1, fade_samples)
    note[:fade_samples] *= fade_in
    fade_out = np.linspace(1, 0, fade_samples)
    note[-fade_samples:] *= fade_out

    sd.play(note, sample_rate)
    sd.wait()