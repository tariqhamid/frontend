import config from 'common/utils/config';
import fetchJson from 'common/utils/fetch-json';
import reportError from 'common/utils/report-error';

const tagPageStats = () => {
    const requestOptions = {
        mode: 'cors',
    };
    const firstContainer = document.querySelector(
        '.js-insert-team-stats-after'
    );

    if (firstContainer) {
        fetchJson(
            `/${config.page.pageId}/fixtures-and-results-container`,
            requestOptions
        )
            .then(container => {
                if (container.html) {
                    firstContainer.insertAdjacentHTML(
                        'afterend',
                        container.html
                    );
                }
            })
            .catch(error => reportError(error, { feature: 'tag-fixtures' }));
    }
};

export default tagPageStats;
