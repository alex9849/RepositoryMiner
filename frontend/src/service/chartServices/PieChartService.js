class PieChartService {

  exampleBackendData = {
    name: 'Pie chart',
    type: 'pie',
    description: 'A pie chart',
    series: [{
      name: 'Citizens',
      data: [{
        name: 'Berlin',
        value: 3500000,
      }, {
        name: 'München',
        value: 1800000
      }, {
        name: 'Kaiserslautern',
        value: 100000
      }]
    }]
  }

  parseBackendToOptions(backendData) {
    let graphConfig = {
      chart: {
        type: 'pie'
      },
      plotOptions: {
        pie: {
          allowPointSelect: true,
          cursor: 'pointer',
          dataLabels: {
            enabled: true,
            format: '<b>{point.name}</b>: {point.percentage:.1f} %'
          }
        }
      }
    }

    let series = [];
    graphConfig.series = series;

    for(let seriesEntry of backendData.series) {
      let data = [];
      let pushData = {
        name: seriesEntry.name,
        data: data
      }
      for(let dataEntry of seriesEntry.data) {
        data.push({
          name: dataEntry.name,
          y: dataEntry.value
        })
      }
      series.push(pushData);
    }
    return {
      graphConfig: graphConfig,
      name: backendData.name,
      description: backendData.description
    }
  }

}

export default new PieChartService();
