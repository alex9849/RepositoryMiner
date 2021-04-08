class PackedBubbleService {

  exampleBackendData = {
    name: 'TestGraph',
    description: 'Test description',
    type: 'packedbubble',
    series: [{
      name: 'Europa',
      data: [{
        name: 'DE',
        value: 20,
        drilldown: {
          name: 'DE',
          data: [{
            name: 'RLP',
            value: 5
          }, {
            name: 'BW',
            value: 10
          }]
        }
      }, {
        name: 'FR',
        value: 25
      }, {
        name: 'ES',
        value: 15
      }]
    }, {
      name: 'Europa2',
      data: [{
        name: 'DE',
        value: 20,
        drilldown: {
          name: 'DEee',
          data: [{
            name: 'RLP',
            value: 5
          }, {
            name: 'BW',
            value: 10
          }]
        }
      }, {
        name: 'FR',
        value: 25
      }, {
        name: 'ES',
        value: 15
      }]
    }]
  };

  parseBackendToOptions(backendData) {
    let options = {
      name: backendData.name,
      description: backendData.description
    };
    let graphConfig = {
      chart: {
        type: 'packedbubble'
      },
      tooltip: {
        useHTML: true
      },
      plotOptions: {
        series: {
          draggable: false,
          layoutAlgorithm: {
            gravitationalConstant: 0.05,
            enableSimulation: false,
            splitSeries: true,
          },
          dataLabels: {
            enabled: true,
            format: '{point.name}',
            style: {
              color: 'black',
              textOutline: 'none',
              fontWeight: 'normal'
            }
          }
        }
      }
    }
    let series = [];
    let drilldown = {
      series: [],
      nextId: 1
    };
    for(let seriesEntry of backendData.series) {
      let data = [];
      series.push({
        name: seriesEntry.name,
        data: data
      })

      for(let dataEntry of seriesEntry.data) {
        let pushData = {
          name: dataEntry.name,
          value: dataEntry.value
        }

        if(!!dataEntry.drilldown && dataEntry.drilldown.length !== 0) {
          this.recProcessDrillDown(drilldown, pushData, dataEntry.drilldown)
        }

        data.push(pushData)
      }
    }

    graphConfig.series = series;
    graphConfig.drilldown = drilldown;
    options.graphConfig = graphConfig;
    return options;
  }

  recProcessDrillDown(target, pushData, drilldown) {
    let data = [];
    target.series.push({
      id: target.nextId,
      name: drilldown.name,
      data: data
    });
    pushData.drilldown = target.nextId;
    target.nextId++;

    for(let dataEntry of drilldown.data) {
      let ddPushData = {
        name: dataEntry.name,
        value: dataEntry.value
      }

      if(!!dataEntry.drilldown && dataEntry.drilldown.length !== 0) {
        this.recProcessDrilldown(target, ddPushData, dataEntry.drilldown)
      }

      data.push(ddPushData)
    }
  }

}

export default new PackedBubbleService();
