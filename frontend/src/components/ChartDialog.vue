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
          {{ parsedChartOptions.name }}
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
      <q-card-section v-if="!!parsedChartOptions.description && !loading">
        <div class="text-h6">Description:</div>
        {{ parsedChartOptions.description }}
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
        <highcharts :options="parsedChartOptions.graphConfig" :highcharts="hcInstance" />
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
import Highcharts from 'highcharts'
import ChartService from "src/service/ChartService";

require('highcharts/highcharts-more.js')(Highcharts);
require('highcharts/modules/drilldown.js')(Highcharts);

export default {
  name: "ChartDialog",
  props: {
    value: {
      type: Boolean,
      required: true
    },
    loading: {
      type: Boolean,
      default: false
    },
    chartOptions: {
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
    parsedChartOptions() {
      let options = ChartService.parseBackendToOptions(this.chartOptions);
      if(!options) {
        return {
          name: 'Loading...'
        }
      }
      return options;
    }
  }
}
</script>

<style scoped>

</style>
