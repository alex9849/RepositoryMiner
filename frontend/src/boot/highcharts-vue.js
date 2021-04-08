import HighchartsVue from 'highcharts-vue';
import Highcharts from 'highcharts';

const defaultOptions = {
  chart: {
    plotBackgroundColor: null,
    plotBorderWidth: null,
    plotShadow: false,
    type: 'pie'
  },
  title: {
    text: null
  }
};

export default ({ Vue }) => {
  Vue.use(HighchartsVue);
  Highcharts.setOptions(defaultOptions);
};
