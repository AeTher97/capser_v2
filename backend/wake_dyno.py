import urllib.request

print("Making a http request to GCL")
# The API endpoint
url = "https://globalcapsleague.com"

# A GET request to the API
response = urllib.request.urlopen(url).read()

# Print the response
print(response)