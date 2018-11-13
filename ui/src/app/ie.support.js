(function () {
    if (!String.prototype.startsWith) {
        String.prototype.startsWith = function(searchString, position) {
            position = position || 0;
            return this.indexOf(searchString, position) === position;
        };
    }
    if (!String.prototype.endsWith) {
        String.prototype.endsWith = function (suffix) {
            return this.indexOf(suffix, this.length - suffix.length) !== -1;
        };
    }
    if (!String.prototype.repeat) {
        String.prototype.repeat = function(count) {
            if (this == null) {
                throw TypeError();
            }
            var string = String(this);
            // `ToInteger`
            var n = count ? Number(count) : 0;
            if (n != n) { // better `isNaN`
                n = 0;
            }
            // Account for out-of-bounds indices
            if (n < 0 || n == Infinity) {
                throw RangeError();
            }
            var result = '';
            while (n) {
                if (n % 2 == 1) {
                    result += string;
                }
                if (n > 1) {
                    string += string;
                }
                n >>= 1;
            }
            return result;
        };
    }

    (function (arr) {
        arr.forEach(function (item) {
            if (item.hasOwnProperty('remove')) {
                return;
            }
            Object.defineProperty(item, 'remove', {
                configurable: true,
                enumerable: true,
                writable: true,
                value: function remove() {
                    if (this.parentNode !== null)
                        this.parentNode.removeChild(this);
                }
            });
        });
    })([Element.prototype, CharacterData.prototype, DocumentType.prototype]); //eslint-disable-line

})();