# quiz-leaderboard-system
# Problem
This project consumes API data from a quiz system, removes duplicate entries, calculates total scores, and generates a leaderboard.

# Approach
- Polled API 10 times (poll = 0 to 9)
- Added 5-second delay between each request
- Used HashSet to remove duplicates using (roundId + participant)
- Used HashMap to store total scores per participant
- Sorted leaderboard in descending order
- Submitted final result using POST API

# Technologies Used
- Java
- REST API
- JSON Parsing (org.json)

# How to Run
1. Compile:
   javac -cp ".;json-20230227.jar" QuizApp.java

2. Run:
   java -cp ".;json-20230227.jar" QuizApp

# Sample Output
Bob : 295  
Alice : 280  
Charlie : 260  
Total Score: 835

# Key Challenge
Handling duplicate API responses correctly using (roundId + participant) to ensure accurate score calculation.
