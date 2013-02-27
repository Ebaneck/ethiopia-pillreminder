ethiopia-pillreminder
=====================

MOTECH implementation modules for a pill reminder project in Ethiopia

IVR UI Test Notes:

Because call initiation to VerboiceIVRController to handle the call request is done via "redirect.vm," this file name needs to be changed based on which decision tree we are using. 
For example, 
<Redirect method="GET">$path/module/verboice/verboice/ivr?CallSid=$sessionId&amp;tree=DemoTree&amp;ln=en</Redirect>
must be updated to the name of the IVRUITestTree...
