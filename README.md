# MailSender
A simple mail sending app


Configuring a .csv file this way, you can send any number of mails to different accounts, creating a different HTML file for every mail.

CSV FILE FORMAT:

[account_to_send_the_email], 1_argument, 2_argument,...,n_argument

[account_2_to_send_the_email], 1_DiffentArgument, 2_DiffenteArgument,...,n_argument


HTML FILE FORMAT:
...
<body>

<h1> this value : $1 will be replaced for the 1_argument in the .csv file, $2 this for the argument_2 and so on... </h1>
  
  
</body>
...
