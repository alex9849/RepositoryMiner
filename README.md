# Hettel-RepositoryMining
> The Hettel-RepositoryMining is a study project from the high school in Kaiserslautern Germany to detect developers
> in a software-project which changed big parts of the code and therefore gained a massive amount of knowledge.
> Such developers are realy important for the workflow of the project and if they get sick, there will be no one who can 
> exchange them. These persons are also known as head monopolies.
> 
> The tool works for every git repository. Just navigate to the folder where your git repository is located, open the powershell cli 
> or the win cmd shell and paste the following command into it: 
>``git log --pretty=format:'[%h] [%an] (%p) %ad %s' --date="format:%Y-%m-%d %H:%M:%S" -m --numstat --summary --parents >> repolog.log``
>
> This command creates a new file with the name __repolog.log__ in the current directory and puts all the necessary
> commit informations into it. The repolog.log-file is the input for the RepositoryMining-Application.