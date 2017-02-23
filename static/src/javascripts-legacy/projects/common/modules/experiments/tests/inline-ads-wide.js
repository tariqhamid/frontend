define([
    'commercial/modules/commercial-features'
], function (
    commercialFeatures
) {
    return function () {
        this.id = 'InlineAdsWide';
        this.start = '2017-02-23';
        this.expiry = '2017-05-10';
        this.author = 'Lydia Shepherd';
        this.description = 'Compare the performance of two inline ad insertion strategies';
        this.audience = .4;
        this.audienceOffset = 0;
        this.successMeasure = 'Advertising revenue will go through the roof without impacting viewability and click-through rate';
        this.audienceCriteria = '';
        this.dataLinkNames = '';
        this.idealOutcome = 'We see a sensible increase in ad impressions without noticeable drop in viewability and CTR';
        this.hypothesis = 'The current spacefinder rules are too restrictive and a lot of articles don\'t have a single inline MPU';
        this.showForSensitive = true;

        this.canRun = function () {
            return commercialFeatures.articleBodyAdverts;
        };

        var success = function (complete) {
            complete();
        };

        this.variants = [
            {
                id: 'control',
                test: function () {},
                success: success.bind(this)
            },
            // In this variant, we leave the geo most popular component there
            // and offset ads in the right-hand column
            {
                id: 'geo',
                test: function () {},
                success: success.bind(this)
            },
            // Here, the geo most pop is removed and ads are offset to the right
            {
                id: 'nogeo',
                test: function () {},
                success: success.bind(this)
            },
            // Here, the geo most pop is removed and ads remain inline
            {
                id: 'none',
                test: function () {},
                success: success.bind(this)
            }
        ];
    };
});
