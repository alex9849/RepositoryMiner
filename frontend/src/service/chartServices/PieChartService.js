class PieChartService {

  parseBackendToOptions(backendData) {
    let optionsObj = {
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
  }

}

export default new PieChartService();
