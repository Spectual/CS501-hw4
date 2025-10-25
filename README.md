# CS501-hw3

## Q1 LifeTracker – A Lifecycle-Aware Activity Logger

```
./LifeTracker
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

  
