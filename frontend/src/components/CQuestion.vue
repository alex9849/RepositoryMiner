<template>
  <q-dialog
    :value="value"
    @input="$emit('input', $event)"
    @hide="$emit('clickAbort')"
  >
    <q-card>
      <q-card-section class="text-center text-h5 width-desktop">
        {{ question }}
        <q-splitter
          horizontal
          :value="10"
        />
        <slot name="error-area" />
        <slot />
        <div class="q-pa-md q-gutter-sm">
          <slot name="buttons">
            <q-btn
              :color="abortColor"
              @click="() => {$emit('clickAbort');}"
              style="width: 100px"
            >
              {{ abortButtonText }}
            </q-btn>
            <q-btn
              :loading="loading"
              :color="okColor"
              style="width: 100px"
              @click="$emit('clickOk')"
            >
              {{ okButtonText }}
            </q-btn>
          </slot>
        </div>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
export default {
  name: "CQuestion",
  props: {
    value: {
      type: Boolean,
      required: true
    },
    question: {
      type: String,
      required: true
    },
    loading: {
      type: Boolean,
      default: false
    },
    abortButtonText: {
      type: String,
      default: 'Abort'
    },
    abortColor: {
      type: String,
      default: 'grey'
    },
    okButtonText: {
      type: String,
      default: 'OK'
    },
    okColor: {
      type: String,
      default: 'positive'
    }
  }
}
</script>

<style scoped>
@media screen and (min-width: 600px) {
  .width-desktop {
    width: 500px;
  }
}
</style>
