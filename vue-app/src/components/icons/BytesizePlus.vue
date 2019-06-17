<template>
  <svg id="i-plus" xmlns="http://www.w3.org/2000/svg"
    viewBox="0 0 32 32"
    :width="width"
    :height="height"
    fill="none"
    :stroke="strokeColor"
    stroke-linecap="round"
    stroke-linejoin="round"
    :stroke-width="strokeWidth">
    <path d="M16 2 L16 30 M2 16 L30 16" />
  </svg>
</template>

<script>
export default {
  props: {
    width: {
      type: [Number, String],
      default: 16,
    },
    height: {
      type: [Number, String],
      default: 16,
    },
    strokeWidth: {
      type: [Number, String],
      default: 2,
    },
    strokeColor: {
      type: String,
      default: 'currentColor',
    },
  },
};
</script>
