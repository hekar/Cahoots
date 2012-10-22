import os
import subprocess
import update

def main():
    dbname = "postgres"
    username = "postgres"
    pgbin = os.getenv("pgbin")
    var = input("Are you sure you would like to reset the database? (Y/N)  ")
    
    if var.startswith("y") or var.startswith("Y"):
        try:
            print("Dropping Database...")
            subprocess.call([pgbin + "/psql.exe", "--dbname=" + dbname, "--username=" + username, "--tuples-only", "--no-align", "--command=""DROP DATABASE cahoots"""], stdout=subprocess.PIPE)
            print("Creating Database...")
            subprocess.call([pgbin + "/psql.exe", "--dbname=" + dbname, "--username=" + username, "--tuples-only", "--no-align", "--command=""\i ./versions/create.sql"""], stdout=subprocess.PIPE)
            print ("Running Updater...")
            update.main()
        except:
            print("An Error Occured!")
        
if __name__ == "__main__":
    main()