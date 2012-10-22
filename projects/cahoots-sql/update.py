import os
import subprocess

def main():
    dbname = "cahoots"
    username = "postgres"

    pgbin = os.getenv("pgbin")
    proc = subprocess.Popen([pgbin + "/psql.exe", "--dbname=" + dbname, "--username=" + username, "--tuples-only", "--no-align", "--command=""select * from database_version"""], stdout=subprocess.PIPE)
    output = proc.stdout.read().decode("utf-8")
    dbv = int(output)
    newversion = dbv

    print("Cahoots Database Version Updater")
    print("Version:" + output)

    versions = [d for d in [int(dir) for dir in os.listdir("./versions") if dir.isdigit()] if d > dbv]
    versions.sort()

    for version in versions:
        print("Applying changes for version " + str(version))
        newversion = version
        changes  = [c for c in os.listdir("./versions/" + str(version)) if c.endswith(".sql")]
        for change in changes:
            print ("Running script " + change + "...")
            subprocess.call([pgbin + "/psql.exe", "--dbname=" + dbname, "--username=" + username, "--tuples-only", "--no-align", "--command=""\i ./versions/" + str(version) + "/" + change + " """])
            print()
            
    if newversion != dbv:
        print("Updating database version number to " + str(newversion))
        subprocess.call([pgbin + "/psql.exe", "--dbname=" + dbname, "--username=" + username, "--tuples-only", "--no-align", "--command=""UPDATE database_version SET version=" + str(newversion) + " """], stdout=subprocess.PIPE)
    else:
        print("Database version is up to date.")
        
if __name__ == "__main__":
    main()