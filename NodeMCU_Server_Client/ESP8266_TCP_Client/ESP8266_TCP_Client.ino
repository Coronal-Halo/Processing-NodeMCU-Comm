/*
    This sketch sends data via HTTP GET requests to data.sparkfun.com service.
    You need to get streamId and privateKey at data.sparkfun.com and paste them
    below. Or just customize this script to talk to other HTTP servers.
*/

#include <ESP8266WiFi.h>

const char* ssid     = "hyx0822";
const char* password = "hyx109876";

const char* host = "192.168.191.1";

void setup() {
  Serial.begin(115200);
  delay(10);

  // We start by connecting to a WiFi network

  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  /* Explicitly set the ESP8266 to be a WiFi-client, otherwise, it by default,
     would try to act as both a client and an access-point and could cause
     network-issues with your other WiFi-devices on your WiFi-network. */
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}
 
void loop() {
  delay(3000);

  Serial.print("connecting to ");
  Serial.println(host);

  // Use WiFiClient class to create TCP connections
  WiFiClient client;
  const int httpPort = 82;
  if (!client.connect(host, httpPort)) {
    Serial.println("connection failed");
    return;
  }

  // We now create a URI for the request

  // The following will send the request to the server
  /* IMPORTANT: ALWAYS USE client.println() before using client.available()!!!
   * REASON: UNKNOWN. It probably has something to do with the I/O buffer on server/clieint side
   */
  Serial.println("Sending message to server ('Hello' in thes case)");
  client.println(String("Hello"));
  unsigned long timeout = millis();
  while (client.available() == 0) {
    if (millis() - timeout > 10000) {
      Serial.println(">>> Client Timeout !");
      client.stop();
      return;
    }
  }

  // Read all the. lines of the reply from server and print them to Serial
  String line = client.readStringUntil('\n');
  delay(10);
  Serial.print("Response from server is: ");
  Serial.println(line);

  Serial.println("closing connection");
}

