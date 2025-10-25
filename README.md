# CS501-hw3

## Q1 LifeTracker – A Lifecycle-Aware Activity Logger

```
./LifeTracker
```

## Q2 Counter++ – Reactive UI with StateFlow & Coroutines

```
./CounterPlus
```
## Q3 Temperature Dashboard – Simulated Sensor Data with StateFlow and Coroutines

```
./TemperatureDashBoard
```
## **How to Use the Apps**

1. Clone this repository:

```
git clone https://github.com/Spectual/CS501-hw4.git
```

1. Open the root folder in **Android Studio**.
2. Each folder (Badge, Contacts, Login, Scaffold, SplitScreen) is an independent Android project.
3. To run a specific app:
   - Open the folder in Android Studio as a separate project, or
   - Use **File > Open** and choose the app folder you want.
   - Click ▶ Run to launch on emulator or device.

## **Explanation of the Apps**

### **1. LifeTracker**
- **Task:** Build an app that tracks and displays Android Activity lifecycle events in real time.

- **Features:**
  - Uses **LifecycleEventObserver** to capture lifecycle transitions (ON_CREATE, ON_START, ON_RESUME, etc.).
  - Stores all events in a **ViewModel** to persist logs across configuration changes.
  - Displays logs in a **LazyColumn**, with each entry showing:
    - The event name
    - A timestamp
    - A color code representing the event type

  - Shows a **Snackbar** notification whenever a new lifecycle event occurs.

  - Visually updates in real time as the Activity moves through lifecycle states.

### **2. Counter++**
- **Task:** Create a reactive counter app that updates through both user interaction and background auto-increment.

- **Features:**
  - Uses a **ViewModel** to manage and persist the counter state during configuration changes.
  - Implements **StateFlow** for reactive, unidirectional data flow between ViewModel and UI.
  - Provides buttons for:
    - **+1** — manually increment the counter  
    - **–1** — manually decrement the counter  
    - **Reset** — reset the counter to zero
  - Includes an **“Auto” mode** toggle that starts or stops a coroutine-based background increment.
  - When Auto mode is **ON**, a coroutine automatically increases the counter every few seconds (default: 3 s).
  - Displays the **current count** and the **status text** (“Auto mode: ON/OFF”) in real time.
  - Offers a **Settings dialog** to configure the auto-increment interval without leaving the screen.

### **3. Temperature Dashboard**
- **Task:** Build a real-time temperature dashboard that simulates sensor data and visualizes updates in a reactive Compose UI.

- **Features:**
  - Uses a **ViewModel** to manage simulated temperature readings and calculated summary statistics.
  - Implements **StateFlow** for reactive, unidirectional data flow between the ViewModel and UI.
  - Simulates new temperature readings every **2 seconds**, generating random float values between **65°F** and **85°F**.
  - Maintains only the **latest 20 readings**, automatically removing older entries as new data arrives.
  - Displays:
    - A scrolling **list of readings** (timestamp + temperature value)
    - Summary values for **Current**, **Average**, **Minimum**, and **Maximum** temperature
  - Includes a **simple line chart** drawn with a `Canvas` to visualize recent temperature trends.
  - Provides **Pause** and **Resume** buttons to start or stop simulated data streaming.
  - All temperature updates occur in real time using **coroutines** and **StateFlow**, ensuring smooth, reactive UI updates.


### AI Usage

For this assignment, I used **OpenAI’s ChatGPT (GPT-5)** as a study and support tool.

**How I used AI**

- Explain important Android development concepts.
- I provided sample classroom code and requested **step-by-step explanations** of why certain behaviors occur.
- I shared parts of my own Compose code and asked AI for **suggestions to improve UI clarity and layout**, such as adding padding, background colors, borders, and better text alignment.



- **What I kept**

  

  - I used the **conceptual explanations** and **ui improvement suggestions** to refine my own implementation.
  - I reused short snippets but **rewrote them in my own structure** to fit assignment requirements.

  

- **What I discarded**

  

  - I did not copy AI-generated solutions.

  
