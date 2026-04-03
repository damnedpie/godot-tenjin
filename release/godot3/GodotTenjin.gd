extends Node

enum AppStoreType {
	unspecified = 0,
	googleplay = 1,
	amazon = 2,
	other = 3
}

signal plugin_initialized
signal gaid_obtain_failed(message)

var _tenjin : JNISingleton = null
var gaid : String = ""

func _ready() -> void:
	if Engine.has_singleton("GodotTenjin"):
		output("Singleton found")
		_tenjin = Engine.get_singleton("GodotTenjin")
		connectSignals()
		initialize(ProjectSettings.get_setting("Tenjin/AppKey"), "", AppStoreType.googleplay)
		gaid = getGAID()
		output("Device GAID is %s" % gaid)
	else:
		output("Singleton not found")

func output(message:String) -> void:
	print("%s: %s" % [name, message])

# Initializes the SDK. You can specify FB deeplink and app store type.
func initialize(apiKey:String, deeplinkURI:String, appStoreType:int) -> void:
	_tenjin.initialize(apiKey, deeplinkURI, appStoreType)

# Logs a plain string event.
func logEvent(event:String) -> void:
	_tenjin.logEvent(event)

# Logs a string event with integer value.
func logEventWithValue(event:String, value:int) -> void:
	_tenjin.logEventWithValue(event, value)

# Logs a purchase with ID, currency, quantity and unit price.
func logPurchase(sku:String, currencyCode:String, quantity:int, unitPrice:float) -> void:
	_tenjin.logPurchase(sku, currencyCode, quantity, unitPrice)

# Logs a purchase with validation details.
# "purchaseData" is purchase.getOriginalJson() from Google Play Billing Library's Purchase object.
# "signature" is purchase.getSignature() from Google Play Billing Library's Purchase object.
func logPurchaseWithSignature(sku:String, currencyCode:String, quantity:int, unitPrice:float, purchaseData:String, signature:String) -> void:
	_tenjin.logPurchaseWithSignature(sku, currencyCode, quantity, unitPrice, purchaseData, signature)

# Returns Google Advertising ID as a String. If failed to obtain it, returns empty string and emits failure signal.
func getGAID() -> String:
	return _tenjin.getGAID()

# Connects JNISingletone signals with the callback functions in this script.
func connectSignals() -> void:
	_tenjin.connect("plugin_initialized", self, "_on_plugin_initialized")
	_tenjin.connect("gaid_obtain_failed", self, "_on_gaid_obtain_failed")

#---------------#
#---CALLBACKS---#
#---------------#

func _on_plugin_initialized() -> void:
	output("Plugin initialized!")
	emit_signal("plugin_initialized")

func _on_gaid_obtain_failed(message:String) -> void:
	output("Failed to obtain GAID, error: %s" % message)
	emit_signal("gaid_obtain_failed", message)