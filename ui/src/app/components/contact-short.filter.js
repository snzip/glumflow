export default angular.module('thingsboard.filters.contactShort', [])
    .filter('contactShort', ContactShort)
    .name;

/*@ngInject*/
function ContactShort($filter) {
    return function (contact) {
        var contactShort = '';
        if (contact) {
            if (contact.address) {
                contactShort += contact.address;
                contactShort += ' ';
            }
            if (contact.address2) {
                contactShort += contact.address2;
                contactShort += ' ';
            }
            if (contact.city) {
                contactShort += contact.city;
                contactShort += ' ';
            }
            if (contact.state) {
                contactShort += contact.state;
                contactShort += ' ';
            }
            if (contact.zip) {
                contactShort += contact.zip;
                contactShort += ' ';
            }
            if (contact.country) {
                contactShort += contact.country;
            }
        }
        if (contactShort === '') {
            contactShort = $filter('translate')('contact.no-address');
        }
        return contactShort;
    };
}
