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
          {{ chartName }}
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
      <q-card-section v-if="!!description">
        <div class="text-h6">Description:</div>
        {{ description }}
      </q-card-section>
      <q-separator/>
      <q-card-section
        class="row justify-center items-center"
      >
        <highcharts :options="chartOptions" :highcharts="hcInstance" />
      </q-card-section>
      <q-inner-loading :showing="loading">
        <q-spinner-pie size="lg"/>
      </q-inner-loading>
    </q-card>
  </q-dialog>
</template>

<script>
import Highcharts from 'highcharts'

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
    chartOptions: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      hcInstance: Highcharts
    }
  }
}
</script>

<style scoped>

</style>
