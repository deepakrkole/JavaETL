

How to Run:

1. Clone the repository

    git clone https://github.com/deepakrkole/JavaETL.git

2. After cloning, change the file locations in src/main/resources/onfig.properties:

	pathImps:  "PATH for imps directory in your system"
	pathClicks: "Path for clicks directory in your system"
	outPath: "Path for Output directory in your system"
	connPath: "Path for dimensions/connection_type.json file in your system"
	devicePath: "Path for dimensions/devices_type.json file in your system"

3. Run Build.py file with "-p" and give no of processes to run (Paralelism)

4. You are good to go!