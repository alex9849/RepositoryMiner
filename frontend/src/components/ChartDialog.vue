<template>
  <q-dialog
    full-width
    :value="value"
    @input="$emit('input', $event)"
  >
    <q-card
      class="bg-white q-ma-lg"
    >
      <q-bar
        class="bg-black text-white"
      >
        <div
          class="col text-center text-weight-bold"
        >
          {{ othertestOptions.name }}
        </div>
        <q-btn
          dense
          flat
          icon="close"
          class="text-white"
          @click="$emit('input', false)"
        >
          <q-tooltip
            content-class="bg-gray-5 text-white"
          >
            Close
          </q-tooltip>
        </q-btn>
      </q-bar>
      <q-card-section v-if="!!description && !loading">
        <div class="text-h6">Description:</div>
        {{ othertestOptions.description }}
      </q-card-section>
      <q-separator/>
      <q-card-section
        v-if="loading"
        style="height: 200px"
      >
        <q-inner-loading
          showing
        >
          <q-spinner-pie size="50px"/>
        </q-inner-loading>
      </q-card-section>
      <q-card-section
        v-if="!loading"
        class="row justify-center items-center"
      >
        <highcharts :options="othertestOptions.graphConfig" :highcharts="hcInstance" />
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
import Highcharts from 'highcharts'
import PackedBubbleService from "src/service/chartServices/PackedBubbleService";

require('highcharts/highcharts-more.js')(Highcharts);
require('highcharts/modules/drilldown.js')(Highcharts);

export default {
  name: "ChartDialog",
  components: [Highcharts],
  props: {
    value: {
      type: Boolean,
      required: true
    },
    chartName: {
      type: String,
      required: true
    },
    description: {
      type: String,
      required: false
    },
    loading: {
      type: Boolean,
      default: false
    },
    chartType: {
      type: String,
      required: true
    },
    chartSeries: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      hcInstance: Highcharts
    }
  },
  computed: {
    othertestOptions() {
      let recieveData = {
        name: 'TestGraph',
        description: 'Test description',
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
      }
      return PackedBubbleService.parseBackendToOptions(recieveData);
    },
    testOptions() {
      return {
        chart: {
          type: 'packedbubble'
        },
        tooltip: {
          useHTML: true,
          pointFormat: '<b>{point.name}:</b> {point.value}m CO<sub>2</sub>'
        },
        series: [{
          name: 'Europe',
          data: [{
            drilldown: 'testdrill',
            name: 'Germany',
            value: 767.1
          }, {
            name: 'Croatia',
            value: 20.7
          },
            {
              name: "Belgium",
              value: 97.2
            }]
        }],
        drilldown: {
          series: [{
            id: 'testdrill',
            type: 'packedbubble',
            name: 'EU2',
            data: [{
              name: 'Germany',
              value: 767.1,
            }, {
              name: 'Croatia',
              value: 20.7
            }]
          }]}
      }
    },
    chartOptions() {
      return {
        yAxis: {
          title: {
            text: 'Number of Employees'
          }
        },
        xAxis: {
          title: {
            text: 'X-Achse'
          },
          accessibility: {
            rangeDescription: 'Range: 2010 to 2017'
          }
        },
        chart: {
          type: this.chartType
        },
        series: this.chartSeries
      }
    }
  }
}
</script>

<style scoped>

</style>
