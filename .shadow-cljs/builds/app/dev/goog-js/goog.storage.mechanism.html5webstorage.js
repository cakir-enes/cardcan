["^ ","~:resource-id",["~:shadow.build.classpath/resource","goog/storage/mechanism/html5webstorage.js"],"~:js","goog.provide(\"goog.storage.mechanism.HTML5WebStorage\");\ngoog.require(\"goog.asserts\");\ngoog.require(\"goog.iter.Iterator\");\ngoog.require(\"goog.iter.StopIteration\");\ngoog.require(\"goog.storage.mechanism.ErrorCode\");\ngoog.require(\"goog.storage.mechanism.IterableMechanism\");\n/**\n * @struct\n * @constructor\n * @extends {goog.storage.mechanism.IterableMechanism}\n * @param {Storage} storage\n */\ngoog.storage.mechanism.HTML5WebStorage = function(storage) {\n  goog.storage.mechanism.HTML5WebStorage.base(this, \"constructor\");\n  /** @private @type {Storage} */ this.storage_ = storage;\n};\ngoog.inherits(goog.storage.mechanism.HTML5WebStorage, goog.storage.mechanism.IterableMechanism);\n/** @private @const @type {string} */ goog.storage.mechanism.HTML5WebStorage.STORAGE_AVAILABLE_KEY_ = \"__sak\";\n/**\n * @return {boolean}\n */\ngoog.storage.mechanism.HTML5WebStorage.prototype.isAvailable = function() {\n  if (!this.storage_) {\n    return false;\n  }\n  try {\n    this.storage_.setItem(goog.storage.mechanism.HTML5WebStorage.STORAGE_AVAILABLE_KEY_, \"1\");\n    this.storage_.removeItem(goog.storage.mechanism.HTML5WebStorage.STORAGE_AVAILABLE_KEY_);\n    return true;\n  } catch (e) {\n    return false;\n  }\n};\n/** @override */ goog.storage.mechanism.HTML5WebStorage.prototype.set = function(key, value) {\n  try {\n    this.storage_.setItem(key, value);\n  } catch (e) {\n    if (this.storage_.length == 0) {\n      throw goog.storage.mechanism.ErrorCode.STORAGE_DISABLED;\n    } else {\n      throw goog.storage.mechanism.ErrorCode.QUOTA_EXCEEDED;\n    }\n  }\n};\n/** @override */ goog.storage.mechanism.HTML5WebStorage.prototype.get = function(key) {\n  var value = this.storage_.getItem(key);\n  if (typeof value !== \"string\" && value !== null) {\n    throw goog.storage.mechanism.ErrorCode.INVALID_VALUE;\n  }\n  return value;\n};\n/** @override */ goog.storage.mechanism.HTML5WebStorage.prototype.remove = function(key) {\n  this.storage_.removeItem(key);\n};\n/** @override */ goog.storage.mechanism.HTML5WebStorage.prototype.getCount = function() {\n  return this.storage_.length;\n};\n/** @override */ goog.storage.mechanism.HTML5WebStorage.prototype.__iterator__ = function(opt_keys) {\n  var i = 0;\n  var storage = this.storage_;\n  var newIter = new goog.iter.Iterator;\n  newIter.next = function() {\n    if (i >= storage.length) {\n      throw goog.iter.StopIteration;\n    }\n    var key = goog.asserts.assertString(storage.key(i++));\n    if (opt_keys) {\n      return key;\n    }\n    var value = storage.getItem(key);\n    if (typeof value !== \"string\") {\n      throw goog.storage.mechanism.ErrorCode.INVALID_VALUE;\n    }\n    return value;\n  };\n  return newIter;\n};\n/** @override */ goog.storage.mechanism.HTML5WebStorage.prototype.clear = function() {\n  this.storage_.clear();\n};\n/**\n * @param {number} index\n * @return {?string}\n */\ngoog.storage.mechanism.HTML5WebStorage.prototype.key = function(index) {\n  return this.storage_.key(index);\n};\n","~:source","// Copyright 2011 The Closure Library Authors. All Rights Reserved.\n//\n// Licensed under the Apache License, Version 2.0 (the \"License\");\n// you may not use this file except in compliance with the License.\n// You may obtain a copy of the License at\n//\n//      http://www.apache.org/licenses/LICENSE-2.0\n//\n// Unless required by applicable law or agreed to in writing, software\n// distributed under the License is distributed on an \"AS-IS\" BASIS,\n// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n// See the License for the specific language governing permissions and\n// limitations under the License.\n\n/**\n * @fileoverview Base class that implements functionality common\n * across both session and local web storage mechanisms.\n *\n */\n\ngoog.provide('goog.storage.mechanism.HTML5WebStorage');\n\ngoog.require('goog.asserts');\ngoog.require('goog.iter.Iterator');\ngoog.require('goog.iter.StopIteration');\ngoog.require('goog.storage.mechanism.ErrorCode');\ngoog.require('goog.storage.mechanism.IterableMechanism');\n\n\n\n/**\n * Provides a storage mechanism that uses HTML5 Web storage.\n *\n * @param {Storage} storage The Web storage object.\n * @constructor\n * @struct\n * @extends {goog.storage.mechanism.IterableMechanism}\n */\ngoog.storage.mechanism.HTML5WebStorage = function(storage) {\n  goog.storage.mechanism.HTML5WebStorage.base(this, 'constructor');\n\n  /**\n   * The web storage object (window.localStorage or window.sessionStorage).\n   * @private {Storage}\n   */\n  this.storage_ = storage;\n};\ngoog.inherits(\n    goog.storage.mechanism.HTML5WebStorage,\n    goog.storage.mechanism.IterableMechanism);\n\n\n/**\n * The key used to check if the storage instance is available.\n * @private {string}\n * @const\n */\ngoog.storage.mechanism.HTML5WebStorage.STORAGE_AVAILABLE_KEY_ = '__sak';\n\n\n/**\n * Determines whether or not the mechanism is available.\n * It works only if the provided web storage object exists and is enabled.\n *\n * @return {boolean} True if the mechanism is available.\n */\ngoog.storage.mechanism.HTML5WebStorage.prototype.isAvailable = function() {\n  if (!this.storage_) {\n    return false;\n  }\n\n  try {\n    // setItem will throw an exception if we cannot access WebStorage (e.g.,\n    // Safari in private mode).\n    this.storage_.setItem(\n        goog.storage.mechanism.HTML5WebStorage.STORAGE_AVAILABLE_KEY_, '1');\n    this.storage_.removeItem(\n        goog.storage.mechanism.HTML5WebStorage.STORAGE_AVAILABLE_KEY_);\n    return true;\n  } catch (e) {\n    return false;\n  }\n};\n\n\n/** @override */\ngoog.storage.mechanism.HTML5WebStorage.prototype.set = function(key, value) {\n\n  try {\n    // May throw an exception if storage quota is exceeded.\n    this.storage_.setItem(key, value);\n  } catch (e) {\n    // In Safari Private mode, conforming to the W3C spec, invoking\n    // Storage.prototype.setItem will allways throw a QUOTA_EXCEEDED_ERR\n    // exception.  Since it's impossible to verify if we're in private browsing\n    // mode, we throw a different exception if the storage is empty.\n    if (this.storage_.length == 0) {\n      throw goog.storage.mechanism.ErrorCode.STORAGE_DISABLED;\n    } else {\n      throw goog.storage.mechanism.ErrorCode.QUOTA_EXCEEDED;\n    }\n  }\n};\n\n\n/** @override */\ngoog.storage.mechanism.HTML5WebStorage.prototype.get = function(key) {\n  // According to W3C specs, values can be of any type. Since we only save\n  // strings, any other type is a storage error. If we returned nulls for\n  // such keys, i.e., treated them as non-existent, this would lead to a\n  // paradox where a key exists, but it does not when it is retrieved.\n  // http://www.w3.org/TR/2009/WD-webstorage-20091029/#the-storage-interface\n  var value = this.storage_.getItem(key);\n  if (typeof value !== 'string' && value !== null) {\n    throw goog.storage.mechanism.ErrorCode.INVALID_VALUE;\n  }\n  return value;\n};\n\n\n/** @override */\ngoog.storage.mechanism.HTML5WebStorage.prototype.remove = function(key) {\n  this.storage_.removeItem(key);\n};\n\n\n/** @override */\ngoog.storage.mechanism.HTML5WebStorage.prototype.getCount = function() {\n  return this.storage_.length;\n};\n\n\n/** @override */\ngoog.storage.mechanism.HTML5WebStorage.prototype.__iterator__ = function(\n    opt_keys) {\n  var i = 0;\n  var storage = this.storage_;\n  var newIter = new goog.iter.Iterator();\n  newIter.next = function() {\n    if (i >= storage.length) {\n      throw goog.iter.StopIteration;\n    }\n    var key = goog.asserts.assertString(storage.key(i++));\n    if (opt_keys) {\n      return key;\n    }\n    var value = storage.getItem(key);\n    // The value must exist and be a string, otherwise it is a storage error.\n    if (typeof value !== 'string') {\n      throw goog.storage.mechanism.ErrorCode.INVALID_VALUE;\n    }\n    return value;\n  };\n  return newIter;\n};\n\n\n/** @override */\ngoog.storage.mechanism.HTML5WebStorage.prototype.clear = function() {\n  this.storage_.clear();\n};\n\n\n/**\n * Gets the key for a given key index. If an index outside of\n * [0..this.getCount()) is specified, this function returns null.\n * @param {number} index A key index.\n * @return {?string} A storage key, or null if the specified index is out of\n *     range.\n */\ngoog.storage.mechanism.HTML5WebStorage.prototype.key = function(index) {\n  return this.storage_.key(index);\n};\n","~:compiled-at",1574887615750,"~:source-map-json","{\n\"version\":3,\n\"file\":\"goog.storage.mechanism.html5webstorage.js\",\n\"lineCount\":88,\n\"mappings\":\"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\",\n\"sources\":[\"goog/storage/mechanism/html5webstorage.js\"],\n\"sourcesContent\":[\"// Copyright 2011 The Closure Library Authors. All Rights Reserved.\\n//\\n// Licensed under the Apache License, Version 2.0 (the \\\"License\\\");\\n// you may not use this file except in compliance with the License.\\n// You may obtain a copy of the License at\\n//\\n//      http://www.apache.org/licenses/LICENSE-2.0\\n//\\n// Unless required by applicable law or agreed to in writing, software\\n// distributed under the License is distributed on an \\\"AS-IS\\\" BASIS,\\n// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\\n// See the License for the specific language governing permissions and\\n// limitations under the License.\\n\\n/**\\n * @fileoverview Base class that implements functionality common\\n * across both session and local web storage mechanisms.\\n *\\n */\\n\\ngoog.provide('goog.storage.mechanism.HTML5WebStorage');\\n\\ngoog.require('goog.asserts');\\ngoog.require('goog.iter.Iterator');\\ngoog.require('goog.iter.StopIteration');\\ngoog.require('goog.storage.mechanism.ErrorCode');\\ngoog.require('goog.storage.mechanism.IterableMechanism');\\n\\n\\n\\n/**\\n * Provides a storage mechanism that uses HTML5 Web storage.\\n *\\n * @param {Storage} storage The Web storage object.\\n * @constructor\\n * @struct\\n * @extends {goog.storage.mechanism.IterableMechanism}\\n */\\ngoog.storage.mechanism.HTML5WebStorage = function(storage) {\\n  goog.storage.mechanism.HTML5WebStorage.base(this, 'constructor');\\n\\n  /**\\n   * The web storage object (window.localStorage or window.sessionStorage).\\n   * @private {Storage}\\n   */\\n  this.storage_ = storage;\\n};\\ngoog.inherits(\\n    goog.storage.mechanism.HTML5WebStorage,\\n    goog.storage.mechanism.IterableMechanism);\\n\\n\\n/**\\n * The key used to check if the storage instance is available.\\n * @private {string}\\n * @const\\n */\\ngoog.storage.mechanism.HTML5WebStorage.STORAGE_AVAILABLE_KEY_ = '__sak';\\n\\n\\n/**\\n * Determines whether or not the mechanism is available.\\n * It works only if the provided web storage object exists and is enabled.\\n *\\n * @return {boolean} True if the mechanism is available.\\n */\\ngoog.storage.mechanism.HTML5WebStorage.prototype.isAvailable = function() {\\n  if (!this.storage_) {\\n    return false;\\n  }\\n\\n  try {\\n    // setItem will throw an exception if we cannot access WebStorage (e.g.,\\n    // Safari in private mode).\\n    this.storage_.setItem(\\n        goog.storage.mechanism.HTML5WebStorage.STORAGE_AVAILABLE_KEY_, '1');\\n    this.storage_.removeItem(\\n        goog.storage.mechanism.HTML5WebStorage.STORAGE_AVAILABLE_KEY_);\\n    return true;\\n  } catch (e) {\\n    return false;\\n  }\\n};\\n\\n\\n/** @override */\\ngoog.storage.mechanism.HTML5WebStorage.prototype.set = function(key, value) {\\n\\n  try {\\n    // May throw an exception if storage quota is exceeded.\\n    this.storage_.setItem(key, value);\\n  } catch (e) {\\n    // In Safari Private mode, conforming to the W3C spec, invoking\\n    // Storage.prototype.setItem will allways throw a QUOTA_EXCEEDED_ERR\\n    // exception.  Since it's impossible to verify if we're in private browsing\\n    // mode, we throw a different exception if the storage is empty.\\n    if (this.storage_.length == 0) {\\n      throw goog.storage.mechanism.ErrorCode.STORAGE_DISABLED;\\n    } else {\\n      throw goog.storage.mechanism.ErrorCode.QUOTA_EXCEEDED;\\n    }\\n  }\\n};\\n\\n\\n/** @override */\\ngoog.storage.mechanism.HTML5WebStorage.prototype.get = function(key) {\\n  // According to W3C specs, values can be of any type. Since we only save\\n  // strings, any other type is a storage error. If we returned nulls for\\n  // such keys, i.e., treated them as non-existent, this would lead to a\\n  // paradox where a key exists, but it does not when it is retrieved.\\n  // http://www.w3.org/TR/2009/WD-webstorage-20091029/#the-storage-interface\\n  var value = this.storage_.getItem(key);\\n  if (typeof value !== 'string' && value !== null) {\\n    throw goog.storage.mechanism.ErrorCode.INVALID_VALUE;\\n  }\\n  return value;\\n};\\n\\n\\n/** @override */\\ngoog.storage.mechanism.HTML5WebStorage.prototype.remove = function(key) {\\n  this.storage_.removeItem(key);\\n};\\n\\n\\n/** @override */\\ngoog.storage.mechanism.HTML5WebStorage.prototype.getCount = function() {\\n  return this.storage_.length;\\n};\\n\\n\\n/** @override */\\ngoog.storage.mechanism.HTML5WebStorage.prototype.__iterator__ = function(\\n    opt_keys) {\\n  var i = 0;\\n  var storage = this.storage_;\\n  var newIter = new goog.iter.Iterator();\\n  newIter.next = function() {\\n    if (i >= storage.length) {\\n      throw goog.iter.StopIteration;\\n    }\\n    var key = goog.asserts.assertString(storage.key(i++));\\n    if (opt_keys) {\\n      return key;\\n    }\\n    var value = storage.getItem(key);\\n    // The value must exist and be a string, otherwise it is a storage error.\\n    if (typeof value !== 'string') {\\n      throw goog.storage.mechanism.ErrorCode.INVALID_VALUE;\\n    }\\n    return value;\\n  };\\n  return newIter;\\n};\\n\\n\\n/** @override */\\ngoog.storage.mechanism.HTML5WebStorage.prototype.clear = function() {\\n  this.storage_.clear();\\n};\\n\\n\\n/**\\n * Gets the key for a given key index. If an index outside of\\n * [0..this.getCount()) is specified, this function returns null.\\n * @param {number} index A key index.\\n * @return {?string} A storage key, or null if the specified index is out of\\n *     range.\\n */\\ngoog.storage.mechanism.HTML5WebStorage.prototype.key = function(index) {\\n  return this.storage_.key(index);\\n};\\n\"],\n\"names\":[\"goog\",\"provide\",\"require\",\"storage\",\"mechanism\",\"HTML5WebStorage\",\"goog.storage.mechanism.HTML5WebStorage\",\"base\",\"storage_\",\"inherits\",\"IterableMechanism\",\"STORAGE_AVAILABLE_KEY_\",\"prototype\",\"isAvailable\",\"goog.storage.mechanism.HTML5WebStorage.prototype.isAvailable\",\"setItem\",\"removeItem\",\"e\",\"set\",\"goog.storage.mechanism.HTML5WebStorage.prototype.set\",\"key\",\"value\",\"length\",\"ErrorCode\",\"STORAGE_DISABLED\",\"QUOTA_EXCEEDED\",\"get\",\"goog.storage.mechanism.HTML5WebStorage.prototype.get\",\"getItem\",\"INVALID_VALUE\",\"remove\",\"goog.storage.mechanism.HTML5WebStorage.prototype.remove\",\"getCount\",\"goog.storage.mechanism.HTML5WebStorage.prototype.getCount\",\"__iterator__\",\"goog.storage.mechanism.HTML5WebStorage.prototype.__iterator__\",\"opt_keys\",\"i\",\"newIter\",\"iter\",\"Iterator\",\"next\",\"newIter.next\",\"StopIteration\",\"asserts\",\"assertString\",\"clear\",\"goog.storage.mechanism.HTML5WebStorage.prototype.clear\",\"goog.storage.mechanism.HTML5WebStorage.prototype.key\",\"index\"]\n}\n"]