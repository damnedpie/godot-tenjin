extends Node

var tenjinInstance : JNISingleton = null

# It's cleaner to keep keys inside Project Settings, but you can specify it here directly.
onready var apiKey : String = ProjectSettings.get_setting('Tenjin/ApiKey') if ProjectSettings.has_setting('Tenjin/ApiKey') else ""

onready var _facebook = $'/root/facebook' if has_node('/root/facebook') else null


func _ready() -> void:
	pause_mode = Node.PAUSE_MODE_PROCESS
	if(Engine.has_singleton("GodotTenjin")):
		tenjinInstance = Engine.get_singleton("GodotTenjin")
	else:
		print("%s : singletone not found" % name)

	# You can initialize the plugin at any point you want.
	initializePlugin(apiKey)

# Call this to initialize the SDK. Feel free to change how the deeplink is obtained.
func initializePlugin(_apiKey:String) -> void:
	if !tenjinInstance:
		print("%s: Cannot initialize Tenjin: JNI not found" % name)
		return

	var deepLink : String = ""
	if _facebook != null:
		deepLink = _facebook.deep_link_uri()
	tenjinInstance.initializePlugin(_apiKey, deepLink)

# Logs an event with just a name and no other info.
func logEvent(event: String) -> void:
	if tenjinInstance:
		Console.trace("%s : logging event %s" % [name, event])
		tenjinInstance.logEvent(event)

# Logs and event and an integer value with it.
func logEventWithValue(event: String, value: int) -> void:
	if tenjinInstance:
		tenjinInstance.logEventWithValue(event, value)

# Logs an IAP.
func logPurchase(sku: String, currency: String, amount: int, price: float) -> void:
	if tenjinInstance:
		tenjinInstance.logPurchase(sku, currency, amount, price)

# Logs an IAP with the receipt.
func logPurchaseWithSignature(sku: String, currency: String, amount: int, price: float, data: String, signature: String) -> void:
	if tenjinInstance:
		tenjinInstance.logPurchaseWithSignature(sku, currency, amount, price, data, signature)

# Returns Google Advertising ID string.
func getAdID() -> String:
	if tenjinInstance:
		return tenjinInstance.getAdId()
	print("%s: no Tenjin JNI instance, can't obtain GAID" % name)
	return ""